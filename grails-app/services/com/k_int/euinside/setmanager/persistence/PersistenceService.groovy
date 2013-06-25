package com.k_int.euinside.setmanager.persistence

import com.k_int.euinside.setmanager.datamodel.Provider
import com.k_int.euinside.setmanager.datamodel.ProviderSet
import com.k_int.euinside.setmanager.datamodel.Record

import java.security.MessageDigest;
import java.util.Set;
import java.util.LinkedHashSet;

import javax.annotation.PostConstruct

/* This class contains all the routines that conform to Iteration 2,
 * so we now take into account providers and sets, the legacy Iteration 1 calls should call the methods in here
 */
class PersistenceService {

	/**************************************/
	// Record related methods
	/**************************************/
	// Lookup
	
	/**
	 * Lookup a record using the ECK ID assigned to the record
	 * @param providerCode The code assigned to the provider
	 * @param setCode The code assigned tot the set
	 * @param live true if we are interested in the committed set, otherwise false
	 * @param eckId The ID assigned to the record by the ECK on import
	 * @return The record with the given ID or null if none found
	 */
	def Record lookupRecordByEckId(providerCode, setCode, live, eckId) {
		log.debug("PersistenceService::lookupRecordByEckId called, Provider: " + providerCode + ", Set: " + setCode + ", live: " + live.toString() + ",  ECK Id: " + eckId);
		
		def record = null;
		
		// Check that eckId is a number not a string
		if ( eckId != null ) {
			try {
				def eckIdAsNum = eckId.toLong()
				record = Record.findBySetAndLiveAndId(lookupProviderSet(providerCode, setCode), live, eckIdAsNum);
					
			} catch (NumberFormatException nfe) {
				// Not a number - don't care
			}
		}
		return(record);
	}
	
	/**
	 * Lookup a record using the CMS ID assigned to the record
	 * @param providerCode The code assigned to the provider
	 * @param setCode The code assigned tot the set
	 * @param live true if we are interested in the committed set, otherwise false
	 * @param cmsId The ID assigned to the record by the source CMS
	 * @return The record with the given CMS ID or null if none found
	 */
	def Record lookupRecordByCmsId(providerCode, setCode, live, cmsId) {
		log.debug("PersistenceService::lookupRecordByCmsId called, Provider: " + providerCode + ", Set: " + setCode + ", live: " + live.toString() + ",  CMS Id: " + cmsId);
		def record = null;
		if (cmsId != null) {
		
			record = Record.findBySetAndLiveAndCmsId(lookupProviderSet(providerCode, setCode), live, cmsId);
		}
		return(record);
	}
	
	/**
	 * Lookup a record using the Persistent ID assigned to the record
	 * @param providerCode The code assigned to the provider
	 * @param setCode The code assigned tot the set
	 * @param live true if we are interested in the committed set, otherwise false
	 * @param persistentId The persistent ID assigned to the record
	 * @return The record with the given persistent ID or null if none found
	 */
	def Record lookupRecordByPersistentId(providerCode, setCode, live, persistentId) {
		log.debug("PersistenceService::lookupRecordByPersistentId called, Provider: " + providerCode + ", Set: " + setCode + ", live: " + live.toString() + ",  Persistent Id: " + persistentId);
		def record = null;
		if (persistentId != null) {
			record = Record.findBySetAndLiveAndPersistentId(lookupProviderSet(providerCode, setCode), live, persistentId);
		}
		return(record);
	}
	
	/**
	 * Lookup a record using the specified combination of ID and id type
	 * @param providerCode The code assigned to the provider
	 * @param setCode The code assigned tot the set
	 * @param live true if we are interested in the committed set, otherwise false
	 * @param id The ID assigned to the record of the type given in idType
	 * @param idType The type of ID to look for. Possible values are 'eck', 'cms' or 'persistent'
	 * @return The record with the given identifier and type or null if none found
	 */
	def Record lookupRecord(providerCode, setCode, live, id, idType) {
		log.debug("PersistenceService::lookupRecord called, Provider: " + providerCode + ", Set: " + setCode + ", live: " + live.toString() + ",  Id: " + id + ", Type: " + idType);
		
		def record = null;
		
		if ( "eck".equalsIgnoreCase(idType) ) {
			record = lookupRecordByEckId(providerCode, setCode, live, id);
		} else if ( "cms".equalsIgnoreCase(idType) ) {
			record = lookupRecordByCmsId(providerCode, setCode, live, id);
		} else if ( "persistent".equalsIgnoreCase(idType) ) {
			record = lookupRecordByPersistentId(providerCode, setCode, live, id);
		} else {
			log.error("Unrecognised id type specified when looking up a record by id. Specified type: " + idType);
		}
		
		return(record);
	}
	
	/**
	 * Lookup all records that have the specified ID - whatever type of ID matches
	 * @param providerCode The code assigned to the provider
	 * @param setCode The code assigned tot the set
	 * @param live true if we are interested in the committed set, otherwise false
	 * @param id The id to look for whatever type of ID
	 * @return Set<Record> the set of records that match the given search
	 */
	def Set<Record> lookupRecordsAnyIdType(providerCode, setCode, live, id) {
		log.debug("PersistenceService::lookupRecordsAnyIdType called, Provider: " + providerCode + ", Set: " + setCode + ", live: " + live.toString() + ",  Id: " + id);
		
		return(lookupRecords(id, id, id));
	}
	
	/**
	 * Lookup all records that match any of the given IDs making sure that the identifiers
	 * only match the specified type
	 * @param providerCode The code assigned to the provider
	 * @param setCode The code assigned tot the set
	 * @param live true if we are interested in the committed set, otherwise false
	 * @param cmsId The CMS id to look for
	 * @param persistentId The persistent ID to look for
	 * @param eckId The ECK assigned ID to look for
	 * @return Set<Record> the set of records that match the given search
	 */
	def Set<Record> lookupRecords(providerCode, setCode, live, cmsId, persistentId, eckId) {
		log.debug("PersistenceService::lookupRecords called, Provider: " + providerCode + ", Set: " + setCode + ", live: " + live.toString() + ",  CMS Id: " + cmsId + ",  Persistent Id: " + persistentId + ",  ECK Id: " + eckId);
		
		def Set<Record> retval = new LinkedHashSet<Record>();
		
		def tempRes = lookupRecord(providerCode, setCode, live, cmsId, "cms");
		if ( tempRes != null )
			retval.add(tempRes);
		
		tempRes = lookupRecord(providerCode, setCode, live, persistentId, "persistent");
		if ( tempRes != null )
			retval.add(tempRes);
		
		tempRes = lookupRecord(providerCode, setCode, live, eckId, "eck");
		if ( tempRes != null )
			retval.add(tempRes);
		
		return retval;
	}
	
	
	// Creation
	
	/**
	 * Return a new record for use. Doesn't persist the record into the database at this stage
	 * @return A new, blank record. Does not persist any data into the database at this stage
	 */
	def createRecord() {
		log.debug("PersistenceService::createRecord called");
		return new Record();
	}

	def lookupProviderSet(providerCode, setCode) {
		// for the time being we will automatically create the provider, but this should change
		return(lookupProviderSet(providerCode, setCode, true, null));
	}
	
	def lookupProviderSet(providerCode, setCode, createProviderIfNotExists, setDescription) {
		def providerSet = null;
		
		// if we do not have a provider then default it
		if (providerCode == null) {
			providerCode = PROVIDER_CODE_DEFAULT;
		}
		// now check we have a provider
		def provider = Provider.findByCode(providerCode);
		if (provider == null) {
			// Provider does not exist, so do we create it
			if (createProviderIfNotExists) {
				provider = new Provider();
				provider.code = providerCode;
				provider.post = false;
				provider.description = "Auto generated with code: " + providerCode;
				if (!(saveRecord(provider, "Provider", providerCode)).successful) {
					// As we failed to save it, set the provider to null
					provider = null;
				}
			}
		}

		// Look for the set if we have a provider
		if (provider != null) {
			// Default the set if we do not have one
			if (setCode == null) {
				setCode = SET_CODE_DEFAULT;
			}
			
			// Now check that this set exists
			providerSet = ProviderSet.findByProviderAndCode(provider, setCode);
			if (providerSet == null) {
				// ProviderSet does not exist, so do we create it
				providerSet = new ProviderSet();
				providerSet.provider = provider;
				providerSet.code = setCode;
				providerSet.description = ((setDescription == null) ? "Auto generated with code: " + setCode : setDescription);
				if (!(saveRecord(providerSet, "ProviderSet", setCode)).successful) {
					// As we failed to save it, set the providerSet to null
					providerSet = null;
				}
			}
		}
		return(providerSet);
	}
		
	// Save / update
	
	def saveRecord(record, table, key) {
		log.debug("PersistenceService::saveRecord called");
		
		def retval = [ : ];
		def messages = [];
		
		// Try saving the record keeping hold of any errors thrown, etc.
		if ( !record.save(flush: true) ) {
			
			log.error("Failed to create / save " + table + " with key " + key)
			retval.successful = false;
			// Errors...
			record.errors.each() {
				log.error("Error: " + it);
				messages.add(it);
			}
			retval.messages = messages;
		} else {
			// Saved successfully
			retval.successful = true;
			retval.messages = [];
			retval.messages.add("Record stored successfully");
			retval.record = record;
		}
		
		return retval;
	}
	
	def updateRecord(record) {
		log.debug("PersistenceService::updateRecord called");
		// For grails save and update are the same thing so just call saveRecord
		return(saveRecord(record, "Record", record.id));
	}
	
	def saveOrUpdateRecord(record) {
		log.debug("PersistenceService::saveOrUpdateRecord called");
		// For grails save and update are the same thing so just call saveRecord
		return(saveRecord(record, "Record", record.id));
	}

	/**
	 * Calculates the MD5 checksum for the past in byte array
	 * 	
	 * @param data The byte array that the checksum is to be calculated for
	 * 
	 * @return The checksum that has been calculated
	 */
	String calculateChecksum(byte []data) {
		def result = null;
		if (data != null) {
			MessageDigest digest = MessageDigest.getInstance("MD5")
	        digest.update(data, 0, data.size());
			byte[] md5sum = digest.digest()
			BigInteger bigInt = new BigInteger(1, md5sum)
			result = bigInt.toString(16);
		}
		return(result);
	}
	
	def findRecord(set, live, cmsId, eckId, persistentId) {
		def record = null;

		// We should always have a cmsId in our hands, so in theory that should never fail
		if (cmsId != null) {
			record = Record.findBySetAndLiveAndCmsId(set, live, cmsId);
		} else if (eckId != null) {
			try {
				def eckIdAsNum = eckId.toLong()
				record = Record.findBySetAndLiveAndId(set, live, eckIdAsNum);
			} catch (NumberFormatException nfe) {
				// Not a number - don't care
			}
		} else if (persistentId != null) {
			record = Record.findBySetAndLiveAndPersistentId(set, live, persistentId);
		}
		
		// If we did not find a record and we were looking in the working set, try the live live set
		if ((record == null) && !live) {
			record = findRecord(set, true, cmsId, eckId, persistentId);
		}
	
		return(record);
	}
	
	/**
	 * Either creates a new record or updates an existing record depending on the parameters
	 * It first looks to see if the record exists in the working set
	 *
	 * @param parameters An object that can contain the following
	 * 			cmsId ............ The id of the record as known by the cms
	 * 			eckId ............ The id of the record as known by the persistence layer
	 * 			persistentId ..... The persistent id of the record as generated by the persistence module
	 *          set .............. The set this record belongs to
	 * 			providerCode ..... The code for the provider who supplied this record (not required if set provided)
	 * 			setCode .......... The code that identifies the set that this record belongs to (not required if set provided)
	 * 			live ............. true if updating the live set, false if updating the working set
	 *          recordContents ... A byte array that contains the record contents
	 *          deleted .......... true if the item has been deleted
	 *
	 * @return An object containing the following
	 * 		   	successful ......... true if the record was created / updated, false if not
	 * 			messages ........... an array of error messages if we were not successful
	 * 		 	record ............. the record that was saved
	 *          recordFound ........ did the record already exist in the database
	 *          newRecordCreated ... true if a new record has been created 
	 */
	def saveRecord(parameters) {
		def result = null;
		String cmsId = parameters.cmsId;
		String eckId = parameters.eckId;
		String persistentId = parameters.persistentId;
		boolean live = parameters.live;
		byte []recordContents = parameters.recordContents;
		String checksum = calculateChecksum(recordContents);
		ProviderSet set = parameters.set;
		if (set == null) {
			String providerCode = parameters.providerCode;
			String setCode = parameters.setCode;
			set = lookupProviderSet(providerCode, setCode);
		}
		boolean deleted = parameters.deleted;
		if (deleted == null) {
			// We have not been told, whether it has been deleted or not, so we assume it has not been
			deleted = false;
		}
		
		boolean existingRecord = true;
		
		// First see if the record already exists
		Record record = findRecord(set, false, cmsId, eckId, persistentId);
		boolean createNewRecord = false;
		if (record == null) {
			// Only create a new record if we are not deleting it
			createNewRecord = !deleted;
			existingRecord = false;
		} else if (record.live) {
			// We have found a live record, so only create a working one if the checksum or deleted flag have changed
			createNewRecord = ((checksum != record.checksum) || (deleted != record.deleted));
		}
		
		// Do we need to create a new record 
		if (createNewRecord) {
			// We need to create a new record
			record = createRecord();
			record.set = set;
			record.cmsId = cmsId;
		}

		if (record == null) {
			// record dosn't exist and we are deleting it, so treat it as successful
			result = [successful : true];
		} else {
			boolean recordChanged = ((recordContents != null) && (checksum != record.checksum));
			
			// At this point we should always have a record, either an existing one or a new one
			if (persistentId != null) {
				// Set the persistent id as we have one in our hand
				record.persistentId = persistentId;
			}
			
			// Mark whether it has been deleted or not
			// Note: If they delete it and then resupply the record then they need to set the deleted flag to false
			//       or should we automatically assume where we have been supplied a record, then it is no longer deleted
			record.deleted = deleted;
	
			if (deleted) {
				// The record is being deleted, so clear out the record contents
				record.originalData = null;
				record.checksum = null;
				record.convertedData = null;
				
				// Set the validation status to be OK
				record.validationStatus = Record.VALIDATION_STATUS_OK;
			} else if (recordChanged) {
				// We have been supplied record contents and they are different from what it was previously
				record.originalData = recordContents;
				record.checksum = checksum;
				record.convertedData = null;
				
				// Needs to be revalidated
				record.validationStatus = Record.VALIDATION_STATUS_NOT_CHECKED;
			}

			// delete any existing validation errors if we are still not in error (ie. been deleted or updated)
			if (record.validationStatus != Record.VALIDATION_STATUS_ERROR) {
				// Why is there no deleteAll method or have I missed it ?
				record.validationErrors.each() {
					// You need to delete the association before you delete the list item
					record.removeFromValidationErrors(it);
					it.delete(flush : true);
				}
			}
							
			// Not forgetting to update the last update date
			record.lastUpdated = new Date();
			
			// Now we can save the record and return the results to the caller
			result = saveRecord(record, "Record", record.id);
		}
		
		// Let the caller know if this record already existed or not in the result
	    result.recordFound = existingRecord;
		result.newRecordCreated = createNewRecord; 
		return(result);
	}
	
	def checkValidIP(providerCode, remoteAddress) {
		def result = [ : ];
		 
		// First check if this is localhost, if so then it is acceptable
		if ((remoteAddress == "127.0.0.1") || (remoteAddress == "localhost") || (remoteAddress == "0:0:0:0:0:0:0:1")) {
			result.validIP = true;
		} else {
			// Secondly we look to see if it is a valid provider
			def provider = Provider.findByCode(providerCode);
			if (provider == null) {
				result.message = "Provider not valid for manager";
				result.validIP = false;
			} else {
				// Provider is valid so let us lookup whether it is valid for the client to submit requests or not
				if (ProviderValidIP.findByProviderAndIpAddress(provider, remoteAddress) == null) {
					result.message =  "Provider cannot send requests to the set manager from the ip address " + remoteAddress;
					result.validIP = false;
				} else {
					// We have a valid IP address
					result.validIP = true;
				}
			}	
		}
		
		// Let the caller know if this is a validated request
		return(result);		
	}
}
