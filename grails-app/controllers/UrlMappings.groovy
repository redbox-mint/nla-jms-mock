class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
		"/apps/srw/search/peopleaustralia"(controller: "nlaService", action: "search", method: "GET")
		
        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
