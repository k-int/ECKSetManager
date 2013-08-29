class UrlMappings {

	static mappings = {
		"/Set/$provider/$setname/$action/$recordId?" {
			controller = "set"
		}

		"/Help/$action" {
			controller = "help"
		}

        "/Oai/$provider" {
            controller = "OaiPmhServer"
            action = "index"
        }

		"/"(view:"/index")
		"500"(view:'/error')
	}
}
