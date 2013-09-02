package com.k_int.euinside.setmanager.controllers

class OaiPmhServerController {


    def ServerService
    def index() {
        println "here"
        render(view:"/oai/index")
    }

    def server() {

        render(text: ServerService.getOia(params), contentType: "text/xml", encoding: "UTF-8")

    }

}
