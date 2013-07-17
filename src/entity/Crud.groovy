package entity

import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovy.json.*
import groovyx.net.http.*

class Crud {

	def url
	def key
	def entityName
	def http

	Crud(){
		def config = new ConfigSlurper().parse(new File("conf/AppConf.groovy").toURL())
		url = config.api.url
		key = config.api.key
		http = new HTTPBuilder( url )
	}

	public Object create() {

		def entity = new File("conf/${entityName}.json").text

		http.request( POST, JSON )  {
			uri.path           = "provision/${entityName}s"
			headers.'X-ApiKey' = key
			body               = entity
			requestContentType = JSON

			response.success = { resp ->
				println resp.statusLine
				println 'Headers: -----------'
				def splitter = "${entityName}s/"
				def id = resp.headers.Location.split(splitter)[1]
				new File("id").write(id)
				return true
			}

			response.failure = { resp->
				println resp.statusLine
				println resp.data
				return false
			}
		}
	}

	def read(String id){

		if(!id){
			id = new File("id").text
		}

		http.request( GET )  {
			uri.path           = "${entityName}s/${id}"
			body               = entity

			response.success = { resp ->
				println resp.statusLine
				return true
			}

			response.failure = { resp, json ->
				println resp.statusLine
				println new JsonBuilder(json)
				return false
			}
		}
	}

	def update(String id){

		if(!id){
			id = new File("id").text
		}

		def entity = new File("conf/${entityName}.json").text

		http.request( PUT, JSON )  {
			uri.path           = "${entityName}s/${id}"
			body               = entity
			requestContentType = JSON

			response.success = { resp ->
				println resp.statusLine
				return true
			}

			response.failure = { resp, json ->
				println resp.statusLine
				println new JsonBuilder(json)
				return false
			}
		}
	}

	def delete(String id){

		if(!id){
			id = new File("id").text
		}

		http.request( DELETE )  {
			uri.path           = "${entityName}s/${id}"
			body               = entity

			response.success = { resp ->
				println resp.statusLine
				return true
			}

			response.failure = { resp, json ->
				println resp.statusLine
				println new JsonBuilder(json)
				return false
			}
		}
	}
}
