package com.k_int.euinside.setmanager.controllers

class OaiPmhServerController {


    def ServerService
    def index() {

        render(text: ServerService.getOia(params), contentType: "text/xml", encoding: "UTF-8")

    }
}
