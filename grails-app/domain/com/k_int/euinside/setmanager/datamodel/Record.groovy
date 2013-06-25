package com.k_int.euinside.setmanager.datamodel

import java.util.Date;

class Record {

	static public String VALIDATION_STATUS_NOT_CHECKED = "NotChecked";
	static public String VALIDATION_STATUS_ERROR       = "Error";
	static public String VALIDATION_STATUS_IN_PROGRESS = "InProgresss";
	static public String VALIDATION_STATUS_OK          = "OK";

	static public String RECORD_TYPE_EDM  = "EDM";
	static public String RECORD_TYPE_LIDO = "LIDO";
			
	// The set this record belongs to
	static belongsTo = [set : ProviderSet]

	// The validation errors for this record
	static hasMany = [validationErrors : ValidationError]
		
	// The CMS ID for the record
	String cmsId;
	
	// The generated persistent ID for the record
	String persistentId;
	
	// The original contents of the record
	byte[] originalData;
	
	// The converted data that is required by Europeana and the various modules
	byte[] convertedData;
	
	// Has the record been deleted? (true for deleted, not-true for live record)
	Boolean deleted = false;
	
	// Is this record part of the live set
	Boolean live = false;
	
	// The validation status of this record
	String validationStatus = VALIDATION_STATUS_NOT_CHECKED;

	// The checksum (if the checksum for the incoming record is the same as the current record, then we do not regard it as changed)
	String checksum;
	 
	// when this record was last updated
	Date lastUpdated = new Date();
	
	// The type of the original record
	String originalType = RECORD_TYPE_LIDO;
	
	// The type of the converted record
	String convertedType = RECORD_TYPE_EDM;
	
    static constraints = {
		cmsId            nullable : false, maxSize : 255,             unique : ['set', 'live']
		persistentId     nullable : true,  maxSize : 255,             unique : ['set', 'live']
		deleted          nullable : false	
		originalData     nullable : true,  maxSize : 10 * 1024 * 1024 
	    originalType     nullable : true,  maxSize : 10
		convertedData    nullable : true,  maxSize : 10 * 1024 * 1024 
	    convertedType    nullable : true,  maxSize : 10
		live             nullable : false
        validationStatus nullable : false, maxsize : 20
 		checksum         nullable : true,  maxSize : 40
 	    lastUpdated      nullable : false
    }
}
