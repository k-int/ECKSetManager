
package com.k_int.euinside.setmanager.action

import javax.servlet.http.HttpServletResponse;

import com.k_int.euinside.client.HttpResult;
import com.k_int.euinside.client.module.Module;
import com.k_int.euinside.client.module.dataMapping.Format;
import com.k_int.euinside.client.module.dataMapping.Transform;
import com.k_int.euinside.client.module.statistics.Tracker;
import com.k_int.euinside.setmanager.datamodel.ProviderSet;
import com.k_int.euinside.setmanager.datamodel.Record;

class DataMappingService extends ServiceActionBase {
	private static String SET_MANAGER_GROUP_DATA_MAPPING = "DataMapping";
	
	/**
	 * Converts the LIDO record into a EDM record
	 *
	 * @param set ... The set that we need to validate
	 *
	 */
	def process(ProviderSet set) {

		Tracker tracker = new Tracker(Module.DATA_MAPPING.getName(), SET_MANAGER_GROUP_DATA_MAPPING);
		tracker.start();
		int recordsProcessed = 0;
		Record.findAllWhere(set : set, live : false, validationStatus : Record.VALIDATION_STATUS_OK, convertedData : null).each() {
			
			// Increment the record processed count
			recordsProcessed++;
			byte [] convertedeData = Transform.transformWait(set.provider.code, it.id.toString(), Format.LIDO, Format.EDM, it.originalData); 
			if (convertedeData != null) {
				// Transformation has been successful
				it.convertedData = convertedeData;
				tracker.incrementSuccessful();
			}
			
			// Not forgetting to save the record
			saveRecord(it, "Record", it.id);
		}

		// We have finished validating so record the stats, no point recording 0 records processed though
		if (recordsProcessed > 0) {
			tracker.completed();
		}
				
		// return the number of records processed
		return(recordsProcessed);
	}
}
