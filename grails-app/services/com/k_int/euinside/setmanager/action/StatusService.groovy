
package com.k_int.euinside.setmanager.action

import java.util.Date;

import com.k_int.euinside.setmanager.datamodel.ProviderSet;
import com.k_int.euinside.setmanager.datamodel.Record;
import com.k_int.euinside.setmanager.datamodel.SetHistory;
import com.k_int.euinside.setmanager.datamodel.SetLive;
import com.k_int.euinside.setmanager.datamodel.SetWorking;

class StatusService {

	/**
	 * Provides an overview of the status of a set
	 * 
	 * @param set ... The set you want the status for
	 * 
	 * @return A json friendly map for the status of the set 
	 */
    def setStatus(ProviderSet set, String numberOfHistoryItems) {
		// On the working set determine the following
		// 1. Status
		// 2. Number of records
		// 3. Number of records that are valid
		// 4. Number of records that are awaiting validation
		// 5. Number of records that have validation errors
		// 6. Number of records that have been deleted
		// In theory (1) = (2) + (3) + (4) + (5)
		def workingSet = ["status"                            : ((set.workingSet == null) ? ProviderSet.STATUS_DIRTY : set.workingSet.status),
			              "numberOfRecords"                   : Record.countBySetAndLive(set, false),
			              // Not we also check deleted records as deleted records have the validation status set to OK
		                  "numberOfRecordsValid"              : Record.countBySetAndLiveAndValidationStatusAndDeleted(set, false, Record.VALIDATION_STATUS_OK, false),
		                  "numberOfRecordsAwaitingValidation" : Record.countBySetAndLiveAndValidationStatus(set, false, Record.VALIDATION_STATUS_NOT_CHECKED),
		                  "numberOfRecordsValidationErrors"   : Record.countBySetAndLiveAndValidationStatus(set, false, Record.VALIDATION_STATUS_ERROR),
		                  "numberOfRecordsDeleted"            : Record.countBySetAndLiveAndDeleted(set, false, true)];
		
		// On the live set determine the following
		// 1. Status
		// 2. Date Committed
		// 3. Number of Records
		def liveSet = null;
		if (set.liveSet != null) {
			liveSet = ["status"          : set.liveSet.status,
				       "dateCommitted"   : set.liveSet.committed,
			           "numberOfRecords" : Record.countBySetAndLive(set, true)];
		}

		// Now build up the list of queued actions
		def queuedActions = [ ];
		set.queuedActions.each() {
			def action = ["action"             : it.action,
						  "queued"             : it.queued,
						  "contentType"        : it.contentType,
						  "recordsToBeDeleted" : it.recordsToBeDeleted,
						  "deleteAll"          : it.deleteAll];
			queuedActions.push(action);
		}

		// Now build up the history list
		int numberOfHistoryItemsInt = 20;
		if (numberOfHistoryItems != null) {
			try {
				// Convert the passed in parameter to an int
				numberOfHistoryItemsInt = Integer.parseInt(numberOfHistoryItems);
				
				// Ensure we havn't been given a number less than 0
				if (numberOfHistoryItemsInt < 1) {
					numberOfHistoryItemsInt = 20;
				}
			} catch (NumberFormatException e) {
				// No need to do anything as we have already setup the default value 
			}
		}
		def historyItems = [ ];
		def historyCriteria = SetHistory.createCriteria();
		def historyRecords = historyCriteria {
			eq("set", set)
			maxResults(numberOfHistoryItemsInt)
			order("id", "desc")
		}
		historyRecords.each() {
			def historyItem = ["action"          : it.action,
							   "when"            : it.whenPerformed,
							   "numberOfRecords" : it.numberOfRecords,
							   "duration"        : it.duration];
			historyItems.push(historyItem);
		}
		 		
		// Now we can build up the result array
		def setStatus = ["code"          : set.code,
			             "description"   : set.description,
						 "created"       : set.created,
						 "workingSet"    : workingSet,
						 "liveSet"       : liveSet,
						 "queuedActions" : queuedActions,
						 "history"       : historyItems];
		return(setStatus); 
    }
}
