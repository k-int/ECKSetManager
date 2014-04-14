package com.k_int.euinside.setmanager.controllers
import com.k_int.euinside.setmanager.datamodel.Provider

class OaiPmhServerController {


    def ServerService
    def index() {
        println "here"
        render(view:"/oai/index")
    }

    def server() {
        if(Provider.findByCode(params.provider)) {
            render(text: ServerService.getOia(params), contentType: "text/xml", encoding: "UTF-8")
        }
        else{
            render "Provider does not exist."
        }
    }

}
