package com.k_int.euinside.setmanager.datamodel

import java.util.Date;

class SetHistory {

	// The set this queued action belongs to
	static belongsTo = [set : ProviderSet]

	// The action to perform
	String action;

	// When this action occured	
	Date whenPerformed = new Date();

	// Number of records that were worked on by this action	
	Integer numberOfRecords = 0;
	
	// Length of time it took to process this action
	Integer duration;
	
	static mapping = {
		version false 
	}
	
    static constraints = {
		action          maxSize : 20, nullable : false
		whenPerformed                 nullable : false
		numberOfRecords               nullable : false
		duration                      nullable : false
    }
}
