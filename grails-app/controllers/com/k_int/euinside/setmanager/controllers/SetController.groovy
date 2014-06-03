package com.k_int.euinside.setmanager.controllers

import grails.converters.JSON;

import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.servlet.http.HttpServletResponse;

import com.k_int.euinside.setmanager.action.CommitService;
import com.k_int.euinside.setmanager.action.EditService;
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
	def EditService;
	def ListService;
	def PersistenceService;
	def PreviewService;
	def RecordService;
	def StatisticsService;
	def StatusService;
	def UpdateService;
	def ValidationService;
    def DataPushService;

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
	
	def edit() {
		ProviderSet set = determineSet();
		if (set != null) {
			render EditService.edit(set,
								    params.europeanaId,
									params.providerDescription,
									params.collectionId,
									params.password,
									params.providerId,
									params.username,
									params.setDescription,
									params.swordURL) as JSON;
		}
	}
	
	def list() {
		ProviderSet set = determineSet();
		if (set != null) {
			render ListService.list(set, params.live, params.status) as JSON;
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
    def push() {
        ProviderSet set = determineSet();
        if (set != null) {
             render DataPushService.performPush(set, params.swordURL) as JSON;
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
		// This is a mixture of some of the basic statistics returned by the status command and data returned from Europeana
		ProviderSet set = determineSet();
		if (set != null) {
			render StatisticsService.setStatistics(set) as JSON;
		}
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
			def result = null;
			def option = params.option;
			if ((option == null) || option.equals(ValidationService.OPTION_LIST)) {
				result = ValidationService.list(set, params.recordId);
			} else {
				def revalidateAll = option.equals(ValidationService.OPTION_REVALIDATE_ALL);
				def recordsMarked = ValidationService.revalidate(set, revalidateAll, params.recordId);
				result = [message : recordsMarked + " Record" + ((recordsMarked == 1) ? "" : "s") + " queued for validation",
						  records : recordsMarked];
			}
			render result as JSON;
		}
	}

	def test() {
		ProviderSet set = determineSet();
		if (set != null) {
			// throws up a test page
		}
	}

	def testEdit() {
		ProviderSet set = determineSet();
		if (set != null) {
			// throws up a test page
			render view : 'testEdit', model : ['set' : set];
		}
	}

	def testList() {
		ProviderSet set = determineSet();
		if (set != null) {
			// throws up a test page
			def statusOptions = [[id : ListService.REQUESTED_STATUS_ALL,     description : "All"],
				                 [id : ListService.REQUESTED_STATUS_DELETED, description : "Deleted"],
								 [id : ListService.REQUESTED_STATUS_ERROR,   description : "Error"],
								 [id : ListService.REQUESTED_STATUS_PENDING, description : "Pending"],
								 [id : ListService.REQUESTED_STATUS_VALID,   description : "Valid"]];
			
			def liveOptions = [[id : ListService.LIVE_NO,   description : "Working Set"],
			                   [id : ListService.LIVE_ONLY, description : "Live Set"]];
			render view : 'testList', model : ['statusOptions' : statusOptions, 'liveOptions' : liveOptions];
		}
	}

	def testValidate() {
		ProviderSet set = determineSet();
		if (set != null) {
			// throws up a test page
			def options = [[id : ValidationService.OPTION_LIST,           description : "List"],
				           [id : ValidationService.OPTION_REVALIDATE,     description : "Revalidate - invalid records"],
						   [id : ValidationService.OPTION_REVALIDATE_ALL, description : "Revalidate - all records"]];
				   
			render view : 'testValidate', model : ['options' : options];
		}
	}
}
