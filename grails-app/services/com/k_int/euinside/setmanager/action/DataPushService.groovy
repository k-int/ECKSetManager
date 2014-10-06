package com.k_int.euinside.setmanager.action

import com.k_int.euinside.client.ZipRecords
import com.k_int.euinside.client.module.Module;
import com.k_int.euinside.client.module.dataTransformation.Format;
import com.k_int.euinside.client.module.statistics.Tracker;
import com.k_int.euinside.client.module.sword.SWORDPush
import com.k_int.euinside.setmanager.datamodel.Provider
import com.k_int.euinside.setmanager.datamodel.ProviderSet

/**
 * Service responsible for handling data push to CultureGrid SWORD server.
 */
class DataPushService {

	// Have set this to 1, so we get a sensible count of how many records were successful and how many errored
	// Otherwise we only get the count of sword requests, which gives no indication of success or not
	private static int RECORDS_TO_ZIP = 1;
    private static String SET_MANAGER_DATA_PUSH = "Push";
    def grailsApplication;

    /**
     * Retrieve username, password, onBehalf from Provider, and collectionId from set, then push data.
     * @param set
     * @param swordURL
     * @return String message containing a summary of the result of request, as well as error messages.
     */
    def performPush(ProviderSet set, String swordURL) {
        String username = set.provider.usrname;
        String password = set.provider.password;
        String onBehalf = set.provider.onBehalf;
		String pushFormat = set.provider.pushFormat;
        String collectionId = set.collectionId;

        return performPush(set, swordURL, username, password, onBehalf, collectionId, pushFormat);
    }

    def performPush(ProviderSet set,
		            String swordURL,
					String username,
					String password,
					String onBehalfOf,
					String collectionId,
					String pushFormat) {
        Tracker tracker = new Tracker(Module.SET_MANAGER.getName(), SET_MANAGER_DATA_PUSH);
        tracker.start();
		
        //If  SWORD URL param is empty, use the default.
        String location = swordURL;
        if ((location == null) || location.isEmpty()) {
            location = set.provider.swordURL;
			if ((location == null) || location.isEmpty()) {
	            location = grailsApplication.config.swordURL;
	        }
        }
		def pushFormatToSend = pushFormat;
		if ((pushFormatToSend == null) || pushFormatToSend.isEmpty()) {
			pushFormatToSend = Format.LIDO.toString();
		}

        ZipRecords zip = new ZipRecords();
        int recordsZipped = 0;

        SWORDPush swordPush = new SWORDPush(location, username, password, onBehalfOf, collectionId);

        def pushResult = new LinkedHashMap<String>(); // will be used for JSON output
        def errorMessages = new ArrayList<String>();


        def iterationSet = set.records.findAll { it.live == true };
        int remainingRecords = iterationSet.size();
		int notSent = 0;
        iterationSet.each {
            remainingRecords--;
			def recordToSend = null;
			
			// Determine which record we want to send
			if (pushFormatToSend.equals(it.convertedType)) {
				recordToSend = it.convertedData;
			} else {
				// We default to sending the original data
				recordToSend = it.originalData;
			}
			
			// Do we actually have a record to send
            if ((recordToSend != null ) && (it.cmsId != null)) {
				zip.addEntry(it.cmsId, recordToSend);
				recordsZipped ++;
            } else {
				notSent++;
            }

            // If we have reached the number of records we want to send in a zip file or we are on the last record and the zip contains at least 1 record then send the zip
            if ((recordsZipped == RECORDS_TO_ZIP) || ((recordsZipped > 0) && (remainingRecords == 0))) {
				try {
					String errors = swordPush.pushData(zip.getZip(), SWORDPush.DATA_TYPE_ZIP);
					if ( ! errors.equals("") ) errorMessages.add(errors);
				} catch ( IllegalArgumentException e ) {
					pushResult.put("IllegalArgument", e.getMessage());
				} catch ( Exception e ) {
                	pushResult.put("ServerException", e.getMessage());
				}
				zip.initialise();

				// reset records zipped so that we don't call push twice when the last record has not been zipped
				recordsZipped = 0;
            }
        };

        pushResult.put("ErrorMessages", errorMessages);
        pushResult.put("FailedDepostis", swordPush.getFailed());
        pushResult.put("Notsent", notSent);
        pushResult.put("SuccessfulDepostis", swordPush.getSuccessful());

		// We have now finished so we can update the statistics
		tracker.incrementSuccessful(swordPush.getSuccessful());
		tracker.incrementFailed(swordPush.getFailed());
		tracker.completed();

        return pushResult;

    }
}
