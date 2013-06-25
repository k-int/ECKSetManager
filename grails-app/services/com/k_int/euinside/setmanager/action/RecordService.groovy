package com.k_int.euinside.setmanager.action

import com.k_int.euinside.setmanager.datamodel.ProviderSet;
import com.k_int.euinside.setmanager.datamodel.Record;

class RecordService {

    def fetch(ProviderSet set, String cmsId) {
		Record record = Record.findWhere(set : set, live : false, cmsId : cmsId);
		if (record == null) {
			// Try the live set
			record = Record.findWhere(set : set, live : true, cmsId : cmsId);
		}

		return((record == null) ? null : ((record.originalData == null) ? null : new String(record.originalData)));			
    }
}
