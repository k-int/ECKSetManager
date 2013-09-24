
package com.k_int.euinside.setmanager.action

import java.nio.charset.Charset;
import javax.servlet.http.HttpServletResponse;

import com.k_int.euinside.client.HttpResult;
import com.k_int.euinside.client.module.Module;
import com.k_int.euinside.client.module.dataTransformation.Format;
import com.k_int.euinside.client.module.dataTransformation.Transform;
import com.k_int.euinside.client.module.statistics.Tracker;
import com.k_int.euinside.setmanager.datamodel.ProviderSet;
import com.k_int.euinside.setmanager.datamodel.Record;

class DataTransformationService extends ServiceActionBase {
	private static String SET_MANAGER_GROUP_DATA_TRANSFORMATION = "DataTransformation";
	private static Charset UTF8 = Charset.forName("UTF-8");
	private static String providerIdentifier = "unknownEUInsideProvider";

	def grailsApplication;

	def initialise() {
		if (grailsApplication.config.providerIdentifier != null) {
			def providerIdentifierValue = grailsApplication.config.providerIdentifier;
			if ((providerIdentifierValue instanceof String) && !providerIdentifierValue.isEmpty()) {
				providerIdentifier = providerIdentifierValue;
			}
		}
	}
	
	/**
	 * Converts the LIDO record into a EDM record
	 *
	 * @param set ... The set that we need to validate
	 *
	 */
	def process(ProviderSet set) {

		Tracker tracker = new Tracker(Module.DATA_TRANSFORMATION.getName(), SET_MANAGER_GROUP_DATA_TRANSFORMATION);
		tracker.start();
		int recordsProcessed = 0;
		Record.findAllWhere(set : set, live : false, validationStatus : Record.VALIDATION_STATUS_OK, convertedData : null).each() {
			
			// Increment the record processed count
			recordsProcessed++;
			byte [] convertedData = Transform.transformWait(set.provider.code, it.id.toString(), Format.LIDO, Format.EDM, it.originalData); 
			if (convertedData != null) {
				// Transformation has been successful
				byte [] updatedRecord = (new String(convertedData, UTF8)).replace("SET_PROVIDER", providerIdentifier).getBytes(UTF8); 
				it.convertedData = updatedRecord;
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
