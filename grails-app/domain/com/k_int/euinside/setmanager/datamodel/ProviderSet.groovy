package com.k_int.euinside.setmanager.datamodel

import java.util.Date;

class ProviderSet {

	// The stati that a set can have
	static public String STATUS_COMMITTING = "Committing";
	static public String STATUS_COMMITTED  = "Committed";
	static public String STATUS_DIRTY      = "Dirty";
	static public String STATUS_ERROR      = "Error";
	static public String STATUS_HARVESTING = "Harvesting"; // Not currently used as we cannot determine when a harvest is complete 
	static public String STATUS_POST       = "Post";
	static public String STATUS_VALIDATING = "Validating";
	
	// The provider this set belongs to
	static belongsTo = [provider : Provider]
	
	// The working and live sets
	static hasOne = [workingSet : SetWorking,
		             liveSet    : SetLive]

	// The records within the set
	static hasMany = [records : Record,
		              queuedActions : SetQueuedAction,
					  history : SetHistory]

	static mapping = {
		version false
		queuedActions sort : 'id', order : 'asc'
		history       sort : 'id', order : 'desc'
	}

	// The unique code the provider uses for this set
	String code;

	Date created = new Date();
		
	// A description for this set
	String description;
	
    static constraints = {
		code        maxSize : 100, nullable : false, blank : false, unique : ['provider']
		description maxSize : 100, nullable : true,  blank : true
		created                    nullable : false
		workingSet                 nullable : true,                 unique : true
		liveSet                    nullable : true,                 unique : true
    }
}
