package com.k_int.euinside.setmanager.datamodel

/** This class defines the provider that can supply data to the Set Manager */
class Provider {
	// The unique identifier that has been allocated to this provider
	String code;

	// A brief description of the provider
	String description;
		
	// For this provider do we use a post or is the data harvested
	Boolean post = false;

	// The europeana identifier for this provider
	String europeanaId;
	
	// The URL to be used for sword deposits, if none supplied on the data push line
	String swordURL;

	// used for SWORD deposit
    String usrname;
	
    // used for SWORD deposti
    String password;
	
    // used for SWORD deposit
    String onBehalf;
	
	static hasMany = [validIPAddresses : ProviderValidIP,
		              sets             : ProviderSet];
	
	static mapping = {
		version false 
	}
	
    static constraints = {
		code        maxSize : 20,  nullable : false, blank : false, unique : true
		description maxSize : 200, nullable : true,  blank : true
		europeanaId maxSize : 256, nullable : true,  blank : true
		post                       nullable : true,  blank : true
        usrname     maxSize : 20,  nullable : true,  blank : true
        password    maxSize : 20,  nullable : true,  blank : true
        onBehalf    maxsize : 100, nullable : true,  blank : true
        swordURL    maxsize : 512, nullable : true
    }
}
