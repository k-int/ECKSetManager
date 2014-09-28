package com.k_int.euinside.setmanager.action

import com.k_int.euinside.setmanager.datamodel.Provider;
import com.k_int.euinside.setmanager.datamodel.ProviderSet;
import com.k_int.euinside.setmanager.persistence.PersistenceService;

import grails.transaction.Transactional

@Transactional
class EditService {

	def PersistenceService;
	
	/**
	 * Allows the details on the provider and the set to be set
	 * 
	 * @param set The set that needs to be modified
	 * @param europeanaId The europeana id to be used when obtaining the stats from europeana
	 * @param providerDescription The description to be used for the provider
	 * @param pushCollectionIdentifier The collection identifier to be used when the data is pushed
	 * @param pushPassword The password to be used when the data ispushed
	 * @param pushProviderIdentifier The providers identifier to be used when the data is pushed
	 * @param pushUserId The user id to be used when the data is pushed
	 * @param setDescription The description to be used for the set
	 * @param swordURL The sword url to be used when we push the data
	 * @param pushFormat The format in which to send the data to the push recipient
	 * 
	 * @return The details about the provider and set, except the password as a JSON object
	 */
    def edit(ProviderSet set,
			 String europeanaId,
			 String providerDescription,
			 String pushCollectionIdentifier,
			 String pushPassword,
			 String pushProviderIdentifier,
			 String pushUserIdentifier,
			 String setDescription,
			 String swordURL,
			 String pushFormat) {

		def details = [ : ];
		if (set != null) {
			set.description = setIfNotEmpty(setDescription, set.description);
			set.collectionId = setIfNotEmpty(pushCollectionIdentifier, set.collectionId);
			def saveResult = PersistenceService.saveRecord(set, "ProviderSet", set.code);
			if (saveResult.successful == true) {
				Provider provider = set.provider;
				provider.description = setIfNotEmpty(providerDescription, provider.description);
				provider.europeanaId = setIfNotEmpty(europeanaId, provider.europeanaId);
				provider.password = setIfNotEmpty(pushPassword, provider.password);
				provider.onBehalf = setIfNotEmpty(pushProviderIdentifier, provider.onBehalf);
				provider.swordURL = setIfNotEmpty(swordURL, provider.swordURL);
				provider.usrname = setIfNotEmpty(pushUserIdentifier, provider.usrname);
				provider.pushFormat = setIfNotEmpty(pushFormat, provider.pushFormat);
				saveResult = PersistenceService.saveRecord(provider, "Provider", provider.code);
				if (saveResult.successful == true) {
					details = ["europeanaId" : provider.europeanaId,
							   "provider" : provider.code,
							   "providerDescription" : provider.description,
							   "pushCollection" : set.collectionId,
							   "pushProviderIdentifier" : provider.onBehalf,
							   "pushUsername" : provider.usrname, 			
							   "set" : set.code,
							   "setDescription" : set.description,
							   "swordURL" : provider.swordURL];
				}
			}
			if (saveResult.successful == false) {
				details.put("errors", saveResult.messages);
			}	
		}
		return(details);
    }
			 
	private def setIfNotEmpty(String value, String defaultValue) {
		def result = defaultValue;
		if ((value != null) && !value.isEmpty()) {
			result = value;
		}
		return(result);
	}
}
