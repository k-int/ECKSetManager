package com.k_int.euinside.setmanager.datamodel

class ValidationError {

	// The record this error belongs to
	static belongsTo = [record : Record]

	// The error code
	String errorCode;
	
	// Any additional information that may have been returned
	String additionalInformation;
		
	static mapping = {
		version false 
	}
	
    static constraints = {
		additionalInformation maxSize : 1000, nullable : true
		errorCode             maxSize : 40,   nullable : false
    }
}
