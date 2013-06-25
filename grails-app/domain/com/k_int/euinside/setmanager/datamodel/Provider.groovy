package com.k_int.euinside.setmanager.datamodel

/** This class defines the provider that can supply data to the Set Manager */
class Provider {
	// The unique identifier that has been allocated to this provider
	String code;

	// A brief description of the provider
	String description;
		
	// For this provider do we use a post or is the data harvested
	Boolean post = false;

	static hasMany = [validIPAddresses : ProviderValidIP,
		              sets             : ProviderSet];
	
	static mapping = {
		version false 
	}
	
    static constraints = {
		code        maxSize : 20,  nullable : false, blank : false, unique : true
		description maxSize : 200, nullable : true,  blank : true
		post                       nullable : true,  blank : true
    }
}
