package com.k_int.euinside.setmanager.action

import com.k_int.euinside.client.ZipRecords
import com.k_int.euinside.client.module.Module;
import com.k_int.euinside.client.module.statistics.Tracker;
import com.k_int.euinside.client.module.sword.SWORDPush
import com.k_int.euinside.setmanager.datamodel.Provider
import com.k_int.euinside.setmanager.datamodel.ProviderSet

/**
 * Service responsible for handling data push to CultureGrid SWORD server.
 */
class DataPushService {

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
        String collectionId = set.collectionId;

        return performPush(set, swordURL, username, password, onBehalf, collectionId);
    }

    def performPush(ProviderSet set, String swordURL, String username, String password, String onBehalfOf, String collectionId) {
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

        ZipRecords zip = new ZipRecords();
        int recordsZipped = 1;

        SWORDPush swordPush = new SWORDPush(location, username, password, onBehalfOf, collectionId);

        def pushResult = new LinkedHashMap<String>(); // will be used for JSON output
        def errorMessages = new ArrayList<String>();


        def iterationSet = set.records.findAll { it.live = true };
        int size = iterationSet.size();
        iterationSet.each {
            if ( ( it.originalData != null ) && ( it.cmsId != null ) ) {
              zip.addEntry(it.cmsId, it.originalData);
              recordsZipped ++;
            }
            else {
              errorMessages.add("Skipping record ${it.cmsId} :: originalData was NULL - this may indicate a deleted record.");
            }

            // If we are on 10th record, or last record (And at least 1 record has been added to the zip list) send the zip
            if ( recordsZipped % 10 == 0 || ( ( recordsZipped > 0 ) && ( size == 1 ) ) {
              try {
                String errors = swordPush.pushData(zip.getZip(), SWORDPush.DATA_TYPE_ZIP);
                if ( ! errors.equals("") ) errorMessages.add(errors);
              } catch ( IllegalArgumentException e ) {
                pushResult.put("IllegalArgument", e.getMessage());
              } catch ( Exception e ) {
                pushResult.put("ServerException", e.getMessage());
              }
              zip.initialise(); // this might be better inside the try block
              // II: reset records zipped so that we don't call push twice when the 11th record has no originalData
              recordsZipped = 0;
            }
            size --;
          };

        pushResult.put("ErrorMessages", errorMessages);
        pushResult.put("FailedDepostis", swordPush.getFailed());
        pushResult.put("SuccessfulDepostis", swordPush.getSuccessful());

		// We have now finished so we can update the statistics
		tracker.incrementSuccessful(swordPush.getSuccessful());
		tracker.incrementFailed(swordPush.getFailed());
		tracker.completed();

        return pushResult;

    }


}
