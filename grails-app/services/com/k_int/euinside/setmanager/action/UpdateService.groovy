package com.k_int.euinside.setmanager.action

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

import com.k_int.euinside.setmanager.datamodel.ProviderSet;
import com.k_int.euinside.setmanager.datamodel.SetQueuedAction;
import com.k_int.euinside.setmanager.datamodel.SetWorking;
import com.k_int.euinside.setmanager.utils.ChunkedObject;

class UpdateService extends ServiceActionBase {

	static def LIDO_NAMESPACES = ['lido' : 'http://www.lido-schema.org'];
	 
	def queue(ProviderSet set, multipartFiles, inputStream, requestContentType, boolean deleteAll, String recordsToDelete) {

		boolean firstFile = true;
		boolean result = true;
		ChunkedObject recordsToDeleteChunked = new ChunkedObject(recordsToDelete, SetQueuedAction.MAX_DELETED_RECORD_CHARACTERS);
		
		// If we have to may characters in the file size or to may characters in the recordsToDelete string then we have to be clever
		// Each file has to be stored on its own though
		if (multipartFiles == null) {
			// Have we been passed an input stream
			if ((inputStream != null) && (requestContentType != null) && !requestContentType.equalsIgnoreCase("application/octet-stream")) {
				// We may have been passed the file directly
				byte[] fileBytes = IOUtils.toByteArray(inputStream);
				ChunkedObject recordContentsChunked = new ChunkedObject(fileBytes, SetQueuedAction.MAX_IMPORT_FILE_CHUNK_SIZE);
				
				if (!save(set, firstFile && deleteAll, recordContentsChunked, requestContentType, recordsToDeleteChunked)) {
					result = false;
				}

				// No longer dealing with the first file				
				firstFile = false
			}
		} else {
			// We have some files
			multipartFiles.each() {parameterName, multiPartFile ->
				ChunkedObject recordContentsChunked = new ChunkedObject(multiPartFile.getBytes(), SetQueuedAction.MAX_IMPORT_FILE_CHUNK_SIZE);
				String contentType = multiPartFile.contentType;
				
				// We ignore posted files we no content type and thos with the content type of 
				if ((contentType != null) && !contentType.equalsIgnoreCase("application/octet-stream")) { 
					if (!save(set, firstFile && deleteAll, recordContentsChunked, contentType, recordsToDeleteChunked)) {
						result = false;
					}

					// No longer dealing with the first file				
					firstFile = false
				}
			}
		}
		
		// If we have not processed a file, then we still need to save the update action
		if (firstFile) {
			result = save(set, firstFile && deleteAll, new ChunkedObject(), null, recordsToDeleteChunked);
		}

		// Return the result tot he caller		
		return(result);
	}

	private def save(ProviderSet set, boolean deleteAll, ChunkedObject recordContentsChunked, String contentType, ChunkedObject recordsToDeleteChunked) {
		SetQueuedAction queuedAction = null;
		boolean result = true;
	
		// We always go through this loop at least once	
		while ((recordsToDeleteChunked.hasMore() || recordContentsChunked.hasMore() || (queuedAction == null)) &&
			   result) {
			   
			// Create the queued action
			SetQueuedAction continuationAction = new SetQueuedAction();
			continuationAction.set = set;
			continuationAction.queued = new Date();
			continuationAction.parent = queuedAction;
			continuationAction.importFileChunk = recordContentsChunked.getNextChunk();
			continuationAction.recordsToBeDeleted = recordsToDeleteChunked.getNextChunk();
			
			// We only set these fields the first time through
			if (queuedAction == null) {
				continuationAction.action = SetQueuedAction.ACTION_UPDATE;
				continuationAction.deleteAll = deleteAll;
				continuationAction.contentType = contentType;
			} else {
				// For continuation actions we have a different action
				continuationAction.action = SetQueuedAction.ACTION_CONTINUED;
			}

			// We can now save this record
			if (continuationAction.save(flush: true)) {
				// Ensure the right id is set on any continuation record
				queuedAction = continuationAction;
			} else {
				log.error("Failed to create SetQueuedAction record");
				// Errors...
				continuationAction.errors.each() {
					log.error("Error: " + it);
				}
				result = false;
			}
		}
			   
	    // Return the result to the caller
	    return(result);
	}
	
	def process(SetQueuedAction queuedAction) {

		ProviderSet set = queuedAction.set;
		List importFile = queuedAction.importFileChunk;
		String contentType = queuedAction.contentType;
		String recordsToBeDeleted = queuedAction.recordsToBeDeleted;
		boolean deleteAll = queuedAction.deleteAll;
		SetQueuedAction parentAction = queuedAction;
		SetQueuedAction continuationAction = SetQueuedAction.findByParent(queuedAction);
		
		// Now we may have continuation records as the import file and records to be deleted may have been to large
		while (continuationAction != null) {
			// we have a continuation record
			if ((importFile != null) && (continuationAction.importFileChunk != null)) {
				importFile.addAll(continuationAction.importFileChunk);
			}
			if ((recordsToBeDeleted != null) && (continuationAction.recordsToBeDeleted)) {
				recordsToBeDeleted += continuationAction.recordsToBeDeleted;
			}
			
			// Move onto the next continuation record if there is one
			parentAction = continuationAction;
			continuationAction = SetQueuedAction.findByParent(parentAction);
		} 

		// Ensure we have a working set
		if (queuedAction.set.workingSet == null) {
			queuedAction.set.workingSet = new SetWorking();
			queuedAction.set.workingSet.set = queuedAction.set;
			queuedAction.set.workingSet.lastUpdated = new Date();
			if (!queuedAction.set.workingSet.save(flush: true) ) {
				log.error("Failed to create working set");
				// Errors...
				queuedAction.set.workingSet.errors.each() {
					log.error("Error: " + it);
				}
			}
		}
		int totalRecords = 0;		
		def recordsProcessed = [ ]; 
		def recordsToDelete = [ ];
		if (recordsToBeDeleted != null) {
			recordsToDelete = recordsToBeDeleted.tokenize(',');
		}
		if ((deleteAll != null) && deleteAll) {
			// Add all the records in this set to the records to delete
			recordsToDelete.addAll(set.records.collect() {it.cmsId});
		}

		if (importFile != null) {
			byte [] fileAsByteArray = importFile.toArray(new byte[0]);				
			if (contentType.contains("xml")) {
				// We have an xml file
				processRecord(set, fileAsByteArray, recordsProcessed);
				totalRecords++;
			} else if (contentType.contains("zip")) {
				// We have a zip file
				ZipInputStream zipFile = new ZipInputStream(new ByteArrayInputStream(fileAsByteArray));
				ZipEntry entry;
				while ((entry = zipFile.getNextEntry()) != null) {
					// We need to ignore directory entries
					if (!entry.isDirectory()) {
						totalRecords++;
						int recordSize = entry.getSize().intValue();
						def recordContents = new byte[recordSize];
						
						// Bit of a bummer, as I cannot read the whole lot in one go
						// Even though we ask for X bytes it always seem to give us less until the last block is requested
						// The number of bytes it gives us is not a consistent number either, so not sure how it is blocking
						int offset = 0;
						int bytesRead = 1;
						int bytesRemaining = recordSize; 
						while (bytesRead > 0) {
							bytesRead = zipFile.read(recordContents, offset, bytesRemaining);
							if (bytesRead > 0) {
								offset += bytesRead;
								bytesRemaining -= bytesRead;
							}
						}
						
						// Did we actually manage to read the whole file 
						if (bytesRemaining == 0) {
							// That is good we have managed to read the full record
							processRecord(set, recordContents, recordsProcessed);
						} else {
							// TODO: We should flag the update record as being in error in some way
							log.error("Failed to read all the bytes for a file from the zip file");
						}
					}
					
					// We have now finished with this entry
					zipFile.closeEntry();
				}
			}
		}
		
		// Now deal with any deletions
		def actualRecordsToMarkAsDeleted = recordsToDelete.minus(recordsProcessed);
		actualRecordsToMarkAsDeleted.each() {recordId ->
			processRecord(set, recordId, null, false, true, null, recordsProcessed);
		}

		// Mark the set as being Dirty, since it has been updated
		queuedAction.set.workingSet.status = ProviderSet.STATUS_DIRTY;

		// Save the working set
		saveRecord(queuedAction.set.workingSet, "SetWorking", queuedAction.set.workingSet.id);

		// Now we have finished updating, add validate to the queue
		queueValidate(set);
		
		// Return the number of records that were processed
		return(recordsProcessed.size());
	}
		
	private def processRecord(set, recordContents, recordsProcessed) {
		// We have a record to add to the database
		def rootXML = new XmlSlurper().parse(new ByteArrayInputStream(recordContents));
        def rootXMLWithNameSpaceDeclared = rootXML.declareNamespace(LIDO_NAMESPACES);
		def recordIds = rootXMLWithNameSpaceDeclared.'lido:administrativeMetadata'.'lido:recordWrap'.'lido:recordID';
		
		// TODO: Need to verify that we should pickup the cms id where the type is local		
		def localRecordId = recordIds.find{it.'@lido:type' == 'local'}.text();
		if (localRecordId.isEmpty()) {
			localRecordId = rootXMLWithNameSpaceDeclared.'lido:lidoRecID'.find{it.'@lido:type' == 'local'}.text();
			log.info("failed to find a record id using lido record id \"" + localRecordId + "\" instead");
		}

		// The persistence Id lives in the objectPublishedID, there could be many, but we are interested in the first one		
		def PersistentRecordId = rootXMLWithNameSpaceDeclared.'lido:objectPublishedID'.find{true}.text();
		
		// That is good we have a local record id, so we can continue, where do we get the persistence id from
		processRecord(set, localRecordId, (PersistentRecordId.isEmpty() ? null : PersistentRecordId), false, false, recordContents, recordsProcessed);
	}
	
	private def processRecord(set, cmsId, persistentId, live, deleted, recordContents, recordsProcessed) {
		// That is good we have a local record id, so we can continue, where do we get the persistence id from
		def saveResult = saveRecord([cmsId : cmsId,
									 persistentId : persistentId, 
									 set : set,
									 live : live,
									 recordContents : recordContents,
									 deleted : deleted]);
		if ((saveResult.successful && !deleted) ||
		    (saveResult.recordFound && deleted)) {
			recordsProcessed.push(cmsId);
		}
	}
}
