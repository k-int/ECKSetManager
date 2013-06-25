package com.k_int.euinside.setmanager

import java.util.concurrent.locks.ReentrantLock;

import com.k_int.euinside.setmanager.datamodel.SetHistory;
import com.k_int.euinside.setmanager.datamodel.SetQueuedAction;

import com.k_int.euinside.setmanager.action.CommitService;
import com.k_int.euinside.setmanager.action.UpdateService;

class SetQueueProcessorJob {
    static triggers = {
		// Delay for a minute after startup of scheduler, then execute every minute
		simple name: 'Set Manager Queue', startDelay: 60000, repeatInterval: 60000
    }

	// The services that perform the work
	def CommitService;
	def UpdateService;
	def ValidationService;
	
	/** Lock to ensure we only have 1 job processing the queue 
	 *  Having multiple jobs would probably work as long as they were not operating on the same set, you would need a lock per set in this case */ 
	private static ReentrantLock queueLock = new ReentrantLock(); 

	/**
	 * Performs the processing for the Set Controller Queue
	 * This will be called (by default every minute) and will process anything waiting in the set_queued_action table,
	 * when there is nothing left to do it will exit.
	 * Only one task will run at a time, if this is to change it is important that multiple tasks DO NOT occur on the same set at the same time
	 * Multiple tasks can occur at the same time but concurrent tasks MUST be occuring on different sets
	 * So for simplicity sake to begin with, I will only allow 1 task to occur at any 1 time
	 * It shouldn't be that difficult to allow a task per set, if I get time I will look at that
	 * It is important that actions are executed and completed in the order they are added to the queue for a set 
	 * 	
	 */
    def execute() {
		// If the queue lock is held by another thread then do not attempt to do anything, so the job will immediatly exit
		if (queueLock.tryLock()) {
			try {
				// lock is not held by another thread, so we can do some work
				// and we keep going while there is something to do
				boolean workToDo = true;
				while (workToDo) {
					def queuedActions = SetQueuedAction.list(max : 1, sort : "id", order : "asc");
					workToDo =(queuedActions.getTotalCount() > 0);
					if (workToDo) {
						SetQueuedAction queuedAction = queuedActions.get(0);
						def actionClosure = null;
						switch (queuedAction.action) {
							case SetQueuedAction.ACTION_UPDATE:
								actionClosure = {UpdateService.process(queuedAction);}
								break;
								
							case SetQueuedAction.ACTION_COMMIT:
								actionClosure = {CommitService.process(queuedAction.set);}
								break;
								
							case SetQueuedAction.ACTION_VALIDATE:
								actionClosure = {ValidationService.process(queuedAction.set);}
								break;
							
							case SetQueuedAction.ACTION_CONVERT_EDM:
							default:
								log.error("Unknown queued action \"" + queuedAction.action + "\" removed from queue");
								break;
						}
						if (actionClosure != null) {
							def history = new SetHistory();
							history.set = queuedAction.set;
							history.action = queuedAction.action;
							def startTime = System.currentTimeMillis();
							history.numberOfRecords = actionClosure();
							history.duration = System.currentTimeMillis() - startTime;
							if (!history.save(flush: true)) {
								log.error("Failed to create history record")
								// Errors...
								history.errors.each() {
									log.error("Error: " + it);
								}
							}
						}
						
						// We have now finished with the queued action
						queuedAction.delete(flush: true);
					}
				} 
			} finally {
				// If we do not have this in the finally then an exception may come along and leave it locked
				// If that was the case then nothing would get processed until the application was restarted 
				queueLock.unlock()
			}
		}
    }
}
