package com.k_int.euinside.setmanager.controllers

import grails.converters.JSON;

import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.servlet.http.HttpServletResponse;

import com.k_int.euinside.setmanager.action.CommitService;
import com.k_int.euinside.setmanager.action.ListService;
import com.k_int.euinside.setmanager.action.PreviewService;
import com.k_int.euinside.setmanager.action.RecordService;
import com.k_int.euinside.setmanager.action.StatusService;
import com.k_int.euinside.setmanager.action.UpdateService;
import com.k_int.euinside.setmanager.action.ValidationService;

import com.k_int.euinside.setmanager.datamodel.ProviderSet
import com.k_int.euinside.setmanager.datamodel.SetQueuedAction;

import com.k_int.euinside.setmanager.persistence.PersistenceService;

class SetController {

	def CommitService;
	def ListService;
	def PersistenceService;
	def PreviewService;
	def RecordService;
	def StatusService;
	def UpdateService;
	def ValidationService;

	/**
	 * Determines the set the caller wishes to act upon and whether the provider can access it from the IP that are calling from
	 * 	
	 * @return The set to be acted upon
	 */
	private def determineSet() {
		ProviderSet set = null;
		String providerCode = params.provider;
		def validIPResult = PersistenceService.checkValidIP(providerCode, request.getRemoteHost());
		if (validIPResult.validIP) {
			// We have a valid IP address so do something ...
			String setCode = params.setname;
			set = PersistenceService.lookupProviderSet(providerCode, setCode, true, params.setDescription);
			if (set == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Error obtaining set")
			}
		} else {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, validIPResult.message);
		}
		return(set);
	}

	def commit() {
		ProviderSet set = localUpdate();
		if (set != null) {
			// Now we can queue the commit
			CommitService.queue(set);
			
			// Inform the caller that we have queued for processing
			response.sendError(HttpServletResponse.SC_ACCEPTED, "Request has been queued for processing");
		}
	}
	
	def list() {
		ProviderSet set = determineSet();
		if (set != null) {
			render ListService.workingSet(set) as JSON;
		}
	}
	
	def preview() {
		ProviderSet set = determineSet();
		if (set != null) {
			String html = PreviewService.preview(set, params.recordId);;
			if (html.isEmpty()) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unable to locate record with id: " + params.recordId);
			} else {
				render(text        : html,
					   contentType : "text/html",
					   encoding    : "UTF-8");
			}
		}
	}
	
	def record() {
		ProviderSet set = determineSet();
		if (set != null) {
			if ((params.recordId == null) || params.recordId.isEmpty()) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "recordId has not been specified");
			} else {
				def xml = RecordService.fetch(set, params.recordId);
				if (xml == null) {
					response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unable to locate record with id: " + params.recordId);
				} else {
					render(text : xml, contentType : "text/xml");
				}
			}
		}
	}
	
	def statistics() {
		
	}
	
	def status() {
		ProviderSet set = determineSet();
		if (set != null) {
			render StatusService.setStatus(set, params.historyItems) as JSON;
		}
	}

	private def localUpdate() {
		ProviderSet set = determineSet();
		if (set != null) {
			// We have a set so we can continue
			def files = null; 
			if (request instanceof org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest) {
				// Get hold of the supplied files 
				files = request.getFileMap();
			}
				
			// Queue the action to be processed later
			def deleteAll = params.deleteAll;
			UpdateService.queue(set, files, request.getInputStream(), request.getContentType(), ((deleteAll != null) && deleteAll.equalsIgnoreCase("yes")), params.delete);
		}
		return(set);
	}
	
	def update() {
		if (localUpdate() != null) {
			// Inform the caller that we have queued for processing
			response.sendError(HttpServletResponse.SC_ACCEPTED, "Request has been queued for processing");
		}
	}
	
	def validate() {
		ProviderSet set = determineSet();
		if (set != null) {
			render ValidationService.list(set, params.recordId) as JSON;
		}
	}

	def test() {
		ProviderSet set = determineSet();
		if (set != null) {
			// throws up a test page
		}
	}
}
