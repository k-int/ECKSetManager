package com.k_int.euinside.setmanager.action

import java.nio.charset.StandardCharsets;

import com.k_int.euinside.setmanager.datamodel.ProviderSet;
import com.k_int.euinside.setmanager.datamodel.Record;

class RecordService {

    def fetch(ProviderSet set, String cmsId) {
		String xml = null;
		Record record = Record.findWhere(set : set, live : false, cmsId : cmsId);
		if (record == null) {
			// Try the live set
			record = Record.findWhere(set : set, live : true, cmsId : cmsId);
		}

		if ((record != null) && (record.originalData != null)) {
			xml = new String(record.originalData, StandardCharsets.UTF_8);
		}
		return(xml);			
    }
}
