class UrlMappings {

	static mappings = {
		"/Set/$provider/$setname/$action/$recordId?" {
			controller = "set"
		}

		"/Help/$action" {
			controller = "help"
		}

		"/"(view:"/index")
		"500"(view:'/error')
	}
}
