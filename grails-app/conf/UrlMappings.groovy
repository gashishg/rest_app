class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/$controller/$id"(parseRequest:true){
		     action = [GET: "show", PUT:"update", DELETE: "delete"]
		     constraints {
		         id(matches:/\d+/)
		     }
		}

		"/$controller"(parseRequest:true){
		     action = [GET: "list", POST:"save"]		     
		}

		"/"(view:"/index")
		"500"(view:'/error')
	}
}
