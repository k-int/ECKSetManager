package com.k_int.euinside.setmanager.action

import org.apache.http.entity.ContentType;

import com.k_int.euinside.setmanager.datamodel.ProviderSet;
import com.k_int.euinside.setmanager.datamodel.Record;

class PreviewService extends ServiceActionBase {
	def grailsApplication;
	
	private static String PATH_BASE_PREVIEW = "/Preview/default/preview";
	
	String previewURL = null;
		
	/**
	 * Initialiser called by bootstrap to setup this service
	 */
	def initialise() {
		previewURL = grailsApplication.config.coreURL;
		previewURL += PATH_BASE_PREVIEW;
	}


	/**
	 * Calls the preview module for the specified record
	 * 
	 * @param set ..... The set that the record belongs to
	 * @param cmsId ... The record that they want to preview
	 * 
	 * @return The html returned by the preview module
	 */
	def preview(ProviderSet set, String cmsId) {

		String html = "";
		
		// Look in the working set
		Record record = Record.findWhere(set : set, live : false, cmsId : cmsId);
		if (record == null) {
			// Not in the working set, look in the live set
			record = Record.findWhere(set : set, live : true, cmsId : cmsId);
		}
		
		if (record != null) {
			// We do have a record, so call the preview module
			// This is what we do when we are successful			
			def successClosure = { httpResponse, content, message ->
				html = message;
			};
			
			// Now we have everything setup post the file to the validation service
			postFile(previewURL, record.originalData, FILENAME_PREFIX + record.id + FILENAME_POSTFIX, successClosure, ContentType.TEXT_HTML);
		}
		
		// Return the returned html
		return(html);
	}
}
