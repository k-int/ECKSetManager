package com.k_int.euinside.setmanager.action

import com.k_int.euinside.setmanager.datamodel.ProviderSet;
import com.k_int.euinside.setmanager.datamodel.Record;
import com.k_int.euinside.setmanager.datamodel.ValidationError;

class ValidationService extends ServiceActionBase {
	def grailsApplication;
	
	private static String PATH_BASE_VALIDATION = "/Validation/lido/validate";
	
	String validationURL = null;
		
	/**
	 * Initialiser called by bootstrap to setup this service
	 */
	def initialise() {
		validationURL = grailsApplication.config.coreURL;
		validationURL += PATH_BASE_VALIDATION;
	}

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

		int recordsProcessed = 0;		
		Record.findAllWhere(set : set, live : false, validationStatus : Record.VALIDATION_STATUS_NOT_CHECKED).each() {

			// Increment the record processed count
			recordsProcessed++;

			// This is what we do when we are successful			
			def successClosure = { httpResponse, content, message ->
				if (message == "OK") {
					// Validation has been successful
					it.validationStatus = Record.VALIDATION_STATUS_OK;
				} else {
					// Validation failed, record the errors
					it.validationStatus = Record.VALIDATION_STATUS_ERROR;
					ValidationError error = new ValidationError();
					error.errorCode = "Error999";
					error.additionalInformation = message;
					error.record = it;
					saveRecord(error, "ValidationError", it.id + "/" + error.errorCode);
				}
				
				// Not forgetting to save the record
				saveRecord(it, "Record", it.id);
			};
			
			// Now we have everything setup post the file to the validation service
			postFile(validationURL, it.originalData, FILENAME_PREFIX + it.id + FILENAME_POSTFIX, successClosure);
		}
		
		// return the number of records processed
		return(recordsProcessed);
	}
}
