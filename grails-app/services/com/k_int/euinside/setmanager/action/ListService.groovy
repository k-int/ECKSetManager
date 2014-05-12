package com.k_int.euinside.setmanager.action

import com.k_int.euinside.setmanager.datamodel.ProviderSet;
import com.k_int.euinside.setmanager.datamodel.Record;

class ListService {
	static public String REQUESTED_STATUS_ALL     = "all";
	static public String REQUESTED_STATUS_DELETED = "deleted";
	static public String REQUESTED_STATUS_ERROR   = "error";
	static public String REQUESTED_STATUS_PENDING = "pending";
	static public String REQUESTED_STATUS_VALID   = "valid";

	static public String LIVE_ONLY = "yes";
	static public String LIVE_NO   = "no";
	
	/**
	 * Returns the brief details for all the records in the working set
	 * 
	 * @param set ... The set that you want the information for
	 * @param liveOnly yes if the caller is only interested in the live set
	 * @param status The status the caller is interested in
	 * 
	 * @return A json compatible array of the brief details for all the records in the working set
	 */
    def list(ProviderSet set, liveOnly, statusRequired) {
		def recordBriefDetails = [ ];
		def recordsFound = [ : ];
		def queryParameters = [ : ];
		queryParameters.put("set", set);
		if (statusRequired != null) {
			switch (statusRequired) {
				case REQUESTED_STATUS_DELETED:
					queryParameters.put("deleted", true);
					break;
					
				case REQUESTED_STATUS_ERROR:
					queryParameters.put("validationStatus", Record.VALIDATION_STATUS_ERROR);
					queryParameters.put("deleted", false);
					break;
					
				case REQUESTED_STATUS_PENDING:
					queryParameters.put("validationStatus", Record.VALIDATION_STATUS_NOT_CHECKED);
					queryParameters.put("deleted", false);
 					break;
					
				case REQUESTED_STATUS_VALID:
					queryParameters.put("validationStatus", Record.VALIDATION_STATUS_OK);
					queryParameters.put("deleted", false);
					break;

				case REQUESTED_STATUS_ALL:
				default: // default needs to be last, otherwise things do not work as expected
					// No need to set any parameters
					break;
			}
		}
		if ((liveOnly == null) || !liveOnly.equalsIgnoreCase(LIVE_ONLY)) {
			queryParameters.put("live", false);
				
			Record.findAllWhere(queryParameters).each() {
				def briefDetails = ["cmsId"            : it.cmsId,
					                "persistentId"     : it.persistentId,
									"lastUpdated"      : it.lastUpdated,
									"deleted"          : it.deleted,
									"validationStatus" : it.validationStatus];
				recordBriefDetails.push(briefDetails);
				recordsFound[it.cmsId] = true;
			}
		}
		queryParameters.put("live", true);
		Record.findAllWhere(queryParameters).each() {
			def briefDetails = ["cmsId"            : it.cmsId,
				                "persistentId"     : it.persistentId,
								"lastUpdated"      : it.lastUpdated,
								"deleted"          : it.deleted,
								"validationStatus" : it.validationStatus];
			// If we have already added this record then ignore it
			if (recordsFound[it.cmsId] == null) {
				recordBriefDetails.push(briefDetails);
			}
		}
		return(recordBriefDetails);
    }
}
