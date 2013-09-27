
import grails.converters.JSON;

import com.k_int.euinside.client.BaseClient;

class BootStrap {

	def grailsApplication;
	
    def init = { servletContext ->
		// Loop through all the services, to see if they need to be initialised
		grailsApplication.serviceClasses.each { 
			def serviceBean = grailsApplication.mainContext.getBean(it.propertyName) 
			if (it.metaClass.respondsTo(serviceBean, 'initialise')) { 
				// We have one that wants initialising and has an initialise method
				serviceBean.initialise() 
			}
		}

		JSON.registerObjectMarshaller(com.k_int.euinside.client.module.europeana.DataSet) {
			return(it.properties.findAll{key, value ->
											((key != "class") && (key != "logger"))
										});
		}
		
		// set the coreURL for the ECKClient
		BaseClient.setCoreBaseURL(grailsApplication.config.coreURL);
    }

	def destroy = {
    }
}
