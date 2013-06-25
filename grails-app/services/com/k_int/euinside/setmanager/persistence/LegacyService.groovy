package com.k_int.euinside.setmanager.persistence

import com.k_int.euinside.setmanager.datamodel.Record

import java.util.Set;
import java.util.LinkedHashSet;

import javax.annotation.PostConstruct

/* This class is now legacy and contains all the routines that were made available for iteration 1
 * for iteration 2 we have the concept of providers and sets, which for the old interface we use the value of DEFAULT
 * for both the provider and the set.
 * This interface is not very useful for managing who provided the records and which set they belonged to
 */
/**
 * Simple class to manage persistence until a persistence module
 * has been implemented
 *
 * @author rpb rich@k-int.com
 * @version 1.0 08.01.13
 *
 */
class LegacyService {

	private static String PROVIDER_CODE_DEFAULT = "DEFAULT";
	private static String SET_CODE_DEFAULT      = "DEFAULT";
	
	def PersistenceService;
	
	/**************************************/
	// Initialisation
	/**************************************/
	@PostConstruct
	def init() {
		log.debug("PersistenceLegacyService::init called");
	}
	
	
	/**************************************/
	// Record related methods
	/**************************************/
	// Lookup
	
	/**
	 * Lookup a record using the ECK ID assigned to the record
	 * @param eckId The ID assigned to the record by the ECK on import
	 * @return The record with the given ID or null if none found
	 */
	def Record lookupRecordByEckId(eckId) {
		log.debug("PersistenceLegacyService::lookupRecordByEckId called with eckId: " + eckId);
		return(PersistenceService.lookupRecordByEckId(PROVIDER_CODE_DEFAULT, SET_CODE_DEFAULT, false, eckId));
	}
	
	/**
	 * Lookup a record using the CMS ID assigned to the record
	 * @param cmsId The ID assigned to the record by the source CMS
	 * @return The record with the given CMS ID or null if none found
	 */
	def Record lookupRecordByCmsId(cmsId) {
		log.debug("PersistenceLegacyService::lookupRecordByCmsId called with cmsId: " + cmsId);
		return(PersistenceService.lookupRecordByCmsId(PROVIDER_CODE_DEFAULT, SET_CODE_DEFAULT, false, cmsId));
	}
	
	/**
	 * Lookup a record using the Persistent ID assigned to the record
	 * @param persistentId The persistent ID assigned to the record
	 * @return The record with the given persistent ID or null if none found
	 */
	def Record lookupRecordByPersistentId(persistentId) {
		log.debug("PersistenceLegacyService::lookupRecordByPersistentId called with persistent id: " + persistentId);
		return(PersistenceService.lookupRecordByPersistentId(PROVIDER_CODE_DEFAULT, SET_CODE_DEFAULT, false, persistentId));
	}
	
	/**
	 * Lookup a record using the specified combination of ID and id type
	 * @param id The ID assigned to the record of the type given in idType
	 * @param idType The type of ID to look for. Possible values are 'eck', 'cms' or 'persistent'
	 * @return The record with the given identifier and type or null if none found
	 */
	def Record lookupRecord(id, idType) {
		log.debug("PersistenceLegacyService::lookupRecord called with id: " + id + " and idType: " + idType);
		
		def retval = null;
		
		if ( "eck".equalsIgnoreCase(idType) ) {
			retval = lookupRecordByEckId(id);
		} else if ( "cms".equalsIgnoreCase(idType) ) {
			retval = lookupRecordByCmsId(id);
		} else if ( "persistent".equalsIgnoreCase(idType) ) {
			retval = lookupRecordByPersistentId(id);
		} else {
			log.error("Unrecognised id type specified when looking up a record by id. Specified type: " + idType);
		}
		
		return retval;
	}
	
	/**
	 * Lookup all records that have the specified ID - whatever type of ID matches
	 * @param id The id to look for whatever type of ID
	 * @return Set<Record> the set of records that match the given search
	 */
	def Set<Record> lookupRecordsAnyIdType(id) {
		log.debug("PersistenceLegacyService::lookupRecords called with id: " + id);
		
		return lookupRecords(id, id, id);
	}
	
	/**
	 * Lookup all records that match any of the given IDs making sure that the identifiers
	 * only match the specified type
	 * @param cmsId The CMS id to look for
	 * @param persistentId The persistent ID to look for
	 * @param eckId The ECK assigned ID to look for
	 * @return Set<Record> the set of records that match the given search
	 */
	def Set<Record> lookupRecords(cmsId, persistentId, eckId) {
		
		def Set<Record> retval = new LinkedHashSet<Record>();
		
		def tempRes = lookupRecord(cmsId, "cms");
		if ( tempRes != null )
			retval.add(tempRes);
		
		tempRes = lookupRecord(persistentId, "persistent");
		if ( tempRes != null )
			retval.add(tempRes);
		
		tempRes = lookupRecord(eckId, "eck");
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
		log.debug("PersistenceLegacyService::createRecord called");
		return(PersistenceService.createRecord());
	}

	// Save / update
	
	def updateRecord(record) {
		log.debug("PersistenceLegacyService::updateRecord called");
		// For grails save and update are the same thing so just call saveRecord
		return(PersistentService.updateRecord(record));
	}
	
	def saveOrUpdateRecord(record) {
		log.debug("PersistenceLegacyService::saveOrUpdateRecord called");
		// For grails save and update are the same thing so just call saveRecord
		return(PersistentService.saveOrUpdateRecord(record));
	}
}
