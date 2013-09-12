package com.k_int.euinside.setmanager.action

import com.k_int.euinside.setmanager.datamodel.ProviderSet;
import com.k_int.euinside.setmanager.datamodel.Record;

class StatisticsService {

	/**
	 * Provides a few statistics for the set as well as going to Europeana to get their view of the data
	 * Just waiting for europeana to get their act together, so that we know what the url is and what will be returned
	 * 
	 * @param set ... The set you want the statistics for
	 * 
	 * @return A json friendly map for the statistics of the set 
	 */
    def setStatistics(ProviderSet set) {
		def workingRecords = Record.countBySetAndLive(set, false);
		def acceptedRecords = Record.countBySetAndLive(set, true);
		def validationErrors = Record.countBySetAndLiveAndValidationStatus(set, false, Record.VALIDATION_STATUS_ERROR);
		def validationNotChecked = Record.countBySetAndLiveAndValidationStatus(set, false, Record.VALIDATION_STATUS_NOT_CHECKED);
		def validationInProgress = Record.countBySetAndLiveAndValidationStatus(set, false, Record.VALIDATION_STATUS_IN_PROGRESS);
		def validationOK = Record.countBySetAndLiveAndValidationStatus(set, false, Record.VALIDATION_STATUS_OK);
	
		// Now we can build up the result array
		// Note: It is possible records can be counted twice in the following scenario:
		// If a record has been committed and then subsequently been updated but the commit has not occurred,
		// In which case it will appear in the accepted count and either in the rejected or pending counts
		// So the total is not the count of all 3 fields
		// Is there a simpler way to get the count of distinct records ?
		def recordCriteria = Record.createCriteria();
		def numberOfRecords = recordCriteria.get {
			sqlRestriction "set_id = ?", [set.id]
			projections {
				countDistinct('cmsId')
			}
		}
		
		def setStatistics = ["name"          : set.code,
			             	 "description"   : set.description,
							 "accepted"      : acceptedRecords,
							 "pending"       : validationNotChecked + validationInProgress + validationOK,
							 "rejected"      : validationErrors,
							 "total"         : numberOfRecords];
						 
		// Need to goto europeana to get the status of the records from them
		return(setStatistics); 
    }
}
