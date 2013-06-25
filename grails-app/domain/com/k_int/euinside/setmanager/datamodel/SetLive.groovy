package com.k_int.euinside.setmanager.datamodel

import java.util.Date;

class SetLive {

	// The provider this set belongs to
	static belongsTo = [set : ProviderSet]
	
	String status = ProviderSet.STATUS_COMMITTED;
	Date committed;

	static mapping = {
		version false 
	}
	
    static constraints = {
		status          maxSize : 20, nullable : false
		committed                     nullable : true
    }
}
