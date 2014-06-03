package com.k_int.euinside.setmanager.action

import javax.servlet.http.HttpServletResponse;

import com.k_int.euinside.client.HttpResult;
import com.k_int.euinside.client.module.Module;
import com.k_int.euinside.client.module.statistics.Tracker;
import com.k_int.euinside.client.module.validation.Validate;
import com.k_int.euinside.client.module.validation.ValidationResult;
import com.k_int.euinside.client.module.validation.ValidationResultRecord;
import com.k_int.euinside.setmanager.datamodel.ProviderSet;
import com.k_int.euinside.setmanager.datamodel.Record;
import com.k_int.euinside.setmanager.datamodel.ValidationError;

class ValidationService extends ServiceActionBase {
	static public String OPTION_LIST           = "list";
	static public String OPTION_REVALIDATE     = "revalidate";
	static public String OPTION_REVALIDATE_ALL = "revalidateall";
	
	def grailsApplication;
	
	private static String SET_MANAGER_GROUP_VALIDATE = "Validate";

	/**
	 * Returns all the validation errors for the specified set
	 * 
	 * @param set ... The set that we want to list the validation errors for
	 *
	 * @return A json compatiblee array of all the records that have validation errors 
	 */
    def list(ProviderSet set) {
		def recordsInError = [ ];
		Record.findAllWhere(set : set, live : false, validationStatus : Record.VALIDATION_STATUS_ERROR).each() {
			buildErrors(it, recordsInError);
		}
		return(recordsInError);
    }

	def revalidate(ProviderSet set, boolean validateAll, String cmsId) {
		def whereParameters = [ : ];
		def recordsMarked = 0;
		whereParameters.put("set", set);
		whereParameters.put("live", false);
		whereParameters.put("deleted", false);
		if (validateAll == false) {
			whereParameters.put("validationStatus", Record.VALIDATION_STATUS_ERROR);
		}
		if ((cmsId != null) && !cmsId.isEmpty()) {
			whereParameters.put("cmsId", cmsId);
		}
		Record.findAllWhere(whereParameters).each() {
			setValidationStatusToNotChecked(it);
			recordsMarked++;
		}
		queueValidate(set);
		return(recordsMarked);
	}
	
	/**
	 * Returns all the validation errors for the specified set and record 
	 * 
	 * @param set ..... The set that we want to list the validation errors for
	 * @param cmsId ... The id of the item that you want the errors from, set to null if you want errors for the whole set 
	 * 
	 * @return A json compatible array of all the records that have validation errors 
	 */
    def list(ProviderSet set, String cmsId) {
		if ((cmsId == null) || cmsId.isEmpty()) {
			return(list(set));
		} else {
			def recordsInError = [ ];
			Record.findAllWhere(set : set, live : false, validationStatus : Record.VALIDATION_STATUS_ERROR, cmsId : cmsId).each() {
				buildErrors(it, recordsInError);
			}
			return(recordsInError);
		}
    }
	
	private def buildErrors(Record record, recordsInError) {
		def errors = [ ];
		record.validationErrors.each() {
			def error = ["errorCode" : it.errorCode,
				         "additionalInformation" : it.additionalInformation];
			errors.push(error);
		}
	
		def details = ["cmsId"        : record.cmsId,
					   "persistentId" : record.persistentId,
					   "lastUpdated"  : record.lastUpdated,
					   "errors"       : errors];
		recordsInError.push(details);
	}

	/**
	 * Validates everything in the set that is waiting to be validated
	 * 
	 * @param set ... The set that we need to validate
	 * 
	 */
	def process(ProviderSet set) {

		Tracker tracker = new Tracker(Module.SET_MANAGER.getName(), SET_MANAGER_GROUP_VALIDATE);
		tracker.start();
		int recordsProcessed = 0;		
		Record.findAllWhere(set : set, live : false, validationStatus : Record.VALIDATION_STATUS_NOT_CHECKED).each() {
			try {
				// Increment the record processed count
				recordsProcessed++;
				ValidationResult validationResult = Validate.sendBytes(set.provider.code, it.originalData);
				if (validationResult != null) {
					List validationRecords = validationResult.getRecords();
					if ((validationRecords != null) && !validationRecords.isEmpty()) {
						// only interested in the first record
						ValidationResultRecord validationResultRecord = validationRecords.get(0);
						if (validationResultRecord.getResult()) {
							// Validation has been successful  
							it.validationStatus = Record.VALIDATION_STATUS_OK;
							tracker.incrementSuccessful();
						} else {
							// Validation failed, record the errors
							markAsError(it, tracker);
							validationResultRecord.getErrors().each() {validationResultRecordError ->
								createError(it, "Error999", validationResultRecordError.getPlugin() + " : " + validationResultRecordError.getText());
							}
						}
					} else {
						markAsError(it, tracker);
						createError(it, "Error997", "Record not returned by the validation module validation module");
					}
				} else {
					markAsError(it, tracker);
					createError(it, "Error998", "Failed to commuicate with the validation module");
				}
			} catch (Exception e) {
				markAsError(it, tracker);
				createError(it, "Error996", "Exception thrown while trying to validate: " + e.toString());
				log.error("Exception thrown while trying to validate", e);
			}

			// Not forgetting to save the record
			saveRecord(it, "Record", it.id);
		}

		// We have finished validating so record the stats, no point recording 0 records processed though
		if (recordsProcessed > 0) {
			tracker.completed();
		}
				
		// return the number of records processed
		return(recordsProcessed);
	}

	private def markAsError(record, tracker) {
		record.validationStatus = Record.VALIDATION_STATUS_ERROR;
		tracker.incrementFailed();
	}
		
	private def createError(record, code, errorText) {
		ValidationError error = new ValidationError();
		error.errorCode = code;
		error.additionalInformation = errorText;
		error.record = record;
		saveRecord(error, "ValidationError", record.id + "/" + code);
	}
}
