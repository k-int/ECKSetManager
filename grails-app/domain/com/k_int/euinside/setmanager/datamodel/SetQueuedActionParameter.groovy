package com.k_int.euinside.setmanager.datamodel

class SetQueuedActionParameter {

	// The queued action this parameter belongs to
	static belongsTo = [queuedAction : SetQueuedAction]

	static mapping = {
		version false
	}

	/** The name of the parameter for the queued action */
	String name;

	/** The value for this parameter */	
	String value;
	
    static constraints = {
		name   maxSize : 50,   nullable : false, blank : false, unique : ['queuedAction']
		value  maxSize : 1000, nullable : false, blank : false
    }
}
