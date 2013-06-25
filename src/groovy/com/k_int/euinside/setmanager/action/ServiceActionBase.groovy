package com.k_int.euinside.setmanager.action

import groovyx.net.http.Method
import groovyx.net.http.HTTPBuilder;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ByteArrayBody
import org.apache.http.entity.mime.HttpMultipartMode
import org.apache.http.entity.mime.MultipartEntity

import com.k_int.euinside.setmanager.datamodel.ProviderSet;
import com.k_int.euinside.setmanager.datamodel.SetQueuedAction;
import com.k_int.euinside.setmanager.persistence.PersistenceService;

class ServiceActionBase {

	static String FILENAME_POSTFIX     = ".xml";
	static String FILENAME_PREFIX      = "record_";
	static String PARAMETER_RECORD     = "record";

	def PersistenceService;
	
	def queueValidate(ProviderSet set) {
		queue(set, SetQueuedAction.ACTION_VALIDATE);
	}
	
	def queueConvertEDM(ProviderSet set) {
		queue(set, SetQueuedAction.ACTION_CONVERT_EDM);
	}

	def queueCommit(ProviderSet set) {
		queue(set, SetQueuedAction.ACTION_COMMIT);
	}
	
	private def queue(ProviderSet set, String action) {
		// Create the queued action
		SetQueuedAction queuedAction = new SetQueuedAction();
		queuedAction.set = set;
		queuedAction.queued = new Date();
		queuedAction.action = action;
	
		// We can now save this record
		saveRecord(queuedAction, "SetQueuedAction", action);
	}
	
	def saveRecord(record, table, key) {
		return(PersistenceService.saveRecord(record, table, key));
	}
	
	def saveRecord(parameters) {
		return(PersistenceService.saveRecord(parameters));
	}
	
	/**
	 * Posts a file to the desired url
	 * 
	 * @param url ................... The url to post the file to
	 * @param file .................. The byte array to post
	 * @param filename .............. The name of the file to associate with the post
	 * @param successClosure ........ The closue to call if we are successful
	 * @param acceptedContentType ... The content type that we are willing to accept (defaults to wildcard) 
	 * @param contentType ........... The content type of the file (default application/xml)
	 * @param parameterName ......... The parameter name to give the file (default "record")
	 * 
	 */
	def postFile(String url,
				 byte[] file,
				 String filename,
				 successClosure,
				 ContentType acceptedContentType = ContentType.WILDCARD,
				 ContentType contentType = ContentType.APPLICATION_XML,
				 String parameterName = PARAMETER_RECORD) {

		// Now we have everything we need, let us perform the post		
		def http = new HTTPBuilder(url);
		
		// We are only interested in text output from the http builder, with regards to html and xml
		http.parser.'application/xml' = http.parser.'text/plain';
		http.parser.'text/xml' = http.parser.'text/plain';
		http.parser.'text/html' = http.parser.'text/plain';
		
		http.request(Method.POST) {req ->
			// It is assumed we are going via the core, so sending it as myltipart shouldn't be a problem
			requestContentType : ContentType.MULTIPART_FORM_DATA 

			// Add the lido contents to the request as the specified parameter
			MultipartEntity multiPartContent = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE)
			
			// Add the file contents to the request as the specified parameter
			multiPartContent.addPart(parameterName, new ByteArrayBody(file, contentType.toString(), filename));
				
			// Now we can add the parts to the request
			req.setEntity(multiPartContent)

			// set what we are willing to accept
			headers.'Accept' = acceptedContentType.toString();
			
			// Deal with the response
			// We need to deal with failures in some sensible way	   
			response.success = { httpResponse, content ->
				String message = "";
				if ((content != null) && (content instanceof java.io.Reader)) {
					message= IOUtils.toString(new ReaderInputStream(content, "UTF-8"), "UTF-8");
				}
				
				// Now call their success closure
				successClosure(httpResponse, content, message);
			}
			
			// handler for any failure status code:
			response.failure = { httpResponse ->
				log.error("Failed to post file to " + url);
			}
		}
	}
}
