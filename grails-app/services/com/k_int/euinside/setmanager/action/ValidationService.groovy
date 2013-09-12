package com.k_int.euinside.setmanager.action

import javax.servlet.http.HttpServletResponse;

import com.k_int.euinside.client.HttpResult;
import com.k_int.euinside.client.module.Module;
import com.k_int.euinside.client.module.statistics.Tracker;
import com.k_int.euinside.client.module.validation.Validate;
import com.k_int.euinside.setmanager.datamodel.ProviderSet;
import com.k_int.euinside.setmanager.datamodel.Record;
import com.k_int.euinside.setmanager.datamodel.ValidationError;

class ValidationService extends ServiceActionBase {
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
			
			// Increment the record processed count
			recordsProcessed++;
			HttpResult validateResult = Validate.sendBytes(it.originalData);
			if ((validateResult.getHttpStatusCode() == HttpServletResponse.SC_OK) && 
			    (validateResult.getContent() == "OK")) {
				// Validation has been successful  
				it.validationStatus = Record.VALIDATION_STATUS_OK;
				tracker.incrementSuccessful();
			} else {
				// Validation failed, record the errors
				it.validationStatus = Record.VALIDATION_STATUS_ERROR;
				tracker.incrementFailed();
				ValidationError error = new ValidationError();
				error.errorCode = "Error999";
				error.additionalInformation = validateResult.getContent();
				error.record = it;
				saveRecord(error, "ValidationError", it.id + "/" + error.errorCode);
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
}
