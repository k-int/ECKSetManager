package com.k_int.euinside.setmanager.action

import java.util.Date;

import com.k_int.euinside.client.module.Module;
import com.k_int.euinside.client.module.statistics.Tracker;
import com.k_int.euinside.setmanager.datamodel.ProviderSet;
import com.k_int.euinside.setmanager.datamodel.Record;
import com.k_int.euinside.setmanager.datamodel.SetLive;

class CommitService extends ServiceActionBase {

	private static String SET_MANAGER_GROUP_COMMIT = "Commit";
	
    def queue(ProviderSet set) {
		// All we do is queue 3 actions
		queueValidate(set);
		queueConvertEDM(set);
		queueCommit(set);
    }
	
	def process(ProviderSet set) {
		Tracker tracker = new Tracker(Module.SET_MANAGER.getName(), SET_MANAGER_GROUP_COMMIT);
		tracker.start();
		
		// Ensure we have a live set
		if (set.liveSet == null) {
			set.liveSet = new SetLive();
			set.liveSet.set = set;
			set.liveSet.committed = new Date();
		}

		// Set the status of the live set to committing
		set.liveSet.status = ProviderSet.STATUS_COMMITTING;
		saveRecord(set.liveSet, "SetLive", set.liveSet.id);
		
		// Set the status of the working set to committing
		set.workingSet.status = ProviderSet.STATUS_COMMITTING;
		saveRecord(set.workingSet, "SetWorking", set.workingSet.id);

		// Keep track of the number of records we have processed
		int numberRecordsProcessed = 0;
				
		// We need to process all the records in the set where validationStatus is OK and move them over to the live set
		Record.findAllWhere(set : set, live : false, validationStatus : Record.VALIDATION_STATUS_OK).each() {
			if (commitRecord(set, it)) {
				// Not forgeting to increment the count of the number of records processed
				numberRecordsProcessed++;
			}
		}

		// Deal with the deleted records
		Record.findAllWhere(set : set, live : false, deleted : true).each() {
			if (commitRecord(set, it)) {
				// Not forgeting to increment the count of the number of records processed
				numberRecordsProcessed++;
			}
		}

		// Set the status to committed for the live set		
		set.liveSet.committed = new Date();
		set.liveSet.status = ProviderSet.STATUS_COMMITTED;
		saveRecord(set.liveSet, "SetLive", set.liveSet.id);

		// Set the status to committed for the working set		
		set.workingSet.lastUpdated = new Date();
		set.workingSet.status = ProviderSet.STATUS_COMMITTED;
		saveRecord(set.workingSet, "SetWorking", set.workingSet.id);

		// Update the statistics
		tracker.incrementSuccessful(numberRecordsProcessed);
		tracker.completed();
		
		// Return the number of records we processed
		return(numberRecordsProcessed);
	}
	
	private def commitRecord(set, record) {
		def processed = false;
		
		// Do not do anything if we have not converted data
		if ((record.convertedData != null) || (record.deleted == true)){
			// If we already have a live record, then copy the working details accross to the live version
			Record liveRecord = Record.findWhere(set : set, live : true, cmsId : record.cmsId);

			// delete the live record and replace it with this one
			if (liveRecord != null) {
				liveRecord.delete();
			}
			
			// nice and easy, just change the live flag to true
			record.live = true;
			saveRecord(record, "Record", record.id);
			
			// Let the caller know it has been processed
			processed = true;
		}
		return(processed);
	}
}
