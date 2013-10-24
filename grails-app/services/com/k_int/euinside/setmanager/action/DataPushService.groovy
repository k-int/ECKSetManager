package com.k_int.euinside.setmanager.action

import com.k_int.euinside.client.ZipRecords
import com.k_int.euinside.client.module.sword.SWORDPush
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
//              Tracker tracker = new Tracker(Module.SET_MANAGER, SET_MANAGER_DATA_PUSH);
//              tracker.start();
                //If  SWORD URL param is empty, use the default.
                String location;
                if ( swordURL == null || swordURL.equals("") ) {
                        location = grailsApplication.config.swordURL;
                } else {
                        location = swordURL;
                }
                ZipRecords zip = new ZipRecords();
                int recordsZipped = 1;

                SWORDPush swordPush = new SWORDPush(location, username, password, onBehalfOf, collectionId);

                def pushResult = new LinkedHashMap<>(); // will be used for JSON output
                def errorMessages = new ArrayList<String>();


                def iterationSet = set.records.findAll { it.live = true };
                int size = iterationSet.size();
                iterationSet.each {
                        zip.addEntry(it.cmsId, it.originalData);
                        // If we are on 10th record, or last record send the zip
                        if ( recordsZipped % 10 == 0 || size == 1 ) {
                                try {
                                        String errors = swordPush.pushData(zip.getZip(), SWORDPush.DATA_TYPE_ZIP);
                                        if ( ! errors.equals("") ) errorMessages.add(errors);
                                } catch ( IllegalArgumentException e ) {
                                        pushResult.put("Illegal argument", e.getMessage());
                                } catch ( Exception e ) {
                                        pushResult.put("Server exception", e.getMessage());
                                }
                                zip.initialise(); // this might be better inside the try block
                        }
                        recordsZipped ++;
                        size --;
                };

                pushResult.put("Error messages", errorMessages);
                pushResult.put("Failed depostis", swordPush.getFailed());
                pushResult.put("Successful depostis", swordPush.getSuccessful());

                return pushResult;

        }


}
