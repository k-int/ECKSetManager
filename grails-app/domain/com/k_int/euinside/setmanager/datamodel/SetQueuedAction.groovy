package com.k_int.euinside.setmanager.datamodel

import java.util.Date;

class SetQueuedAction {

	static String ACTION_COMMIT      = "Commit";
	static String ACTION_CONTINUED   = "Continued";
	static String ACTION_CONVERT_EDM = "ConvertToEDM";
	static String ACTION_UPDATE      = "Update";
	static String ACTION_VALIDATE    = "Validate";
	
	static int MAX_DELETED_RECORD_CHARACTERS = 2000;
	static int MAX_IMPORT_FILE_CHUNK_SIZE    = 10 * 1024 * 1024;
	
	// The set this queued action belongs to
	static belongsTo = [set    : ProviderSet,
		                parent : SetQueuedAction]

	static hasOne = [child : SetQueuedAction];
	
	// The action to perform
	String action;
	
	// Date this qction was queued
	Date queued;

	// File that needs to be interpreted
	byte[] importFileChunk;
	
	// The content type of the file
	String contentType;
	
	// comma delimited list of records that needs to be updated
	String recordsToBeDeleted;
	
	// Do we delete all the records first
	boolean deleteAll = false;
	
	static mapping = {
		version false 
	}
	
    static constraints = {
		action             nullable : false, maxSize : 20
		queued             nullable : false
		importFileChunk    nullable : true,  maxSize : MAX_IMPORT_FILE_CHUNK_SIZE
		contentType        nullable : true,  maxSize : 100
		recordsToBeDeleted nullable : true,  maxSize : MAX_DELETED_RECORD_CHARACTERS
		deleteAll          nullable : true
		parent             nullable : true,  unique : true
		child              nullable : true
    }
}
