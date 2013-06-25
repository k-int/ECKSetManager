package com.k_int.euinside.setmanager.datamodel

/** This class defines which client IP Addresses can call the Set Manager for a provider */
class ProviderValidIP {

	// The provider this IP address is valid for
	static belongsTo = [provider : Provider]
	
	// The IP address that is valid for this provider
	String ipAddress;
	
	static mapping = {
		version false 
	}
	
    static constraints = {
		ipAddress size : 7..23, nullable : false, blank : false
    }
}
