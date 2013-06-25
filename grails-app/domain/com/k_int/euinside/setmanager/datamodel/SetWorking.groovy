package com.k_int.euinside.setmanager.datamodel

import java.util.Date;

class SetWorking {

	// The provider this set belongs to
	static belongsTo = [set : ProviderSet]
	
	String status = ProviderSet.STATUS_DIRTY;
	Date lastUpdated;
	
	static mapping = {
		version false 
	}
	
    static constraints = {
		status                     nullable : false, maxSize : 20
		lastUpdated                nullable : true
    }
}
