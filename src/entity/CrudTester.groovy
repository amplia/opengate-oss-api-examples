package entity

import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovy.json.*
import groovyx.net.http.*
import groovy.text.GStringTemplateEngine

class CrudTester {

	/* CONFIGURATION */
	//private static final String ENVIRONMENT="amplia"
	private static final String ENVIRONMENT="neoris"

	def key
	def fileName
	def http
	def url
	def urlPrefix
	def bindings
	def util = new Util()

	CrudTester(String url, String filePrefix){
		this.url=url
		this.fileName = filePrefix
		configuration()
	}
	
	CrudTester(ServiceUrl serviceUrl, EntityType entityType){
		this.url = serviceUrl.toString() + "/"+ entityType.toString() + "s"
		this.fileName = entityType.toString()
		configuration()
	}
	
	private configuration(){
		def config = new ConfigSlurper(this.ENVIRONMENT).parse(new File("conf/AppConf.groovy").toURL())
		bindings = config.uuids
		urlPrefix = config.api.url
		key = config.api.key
		http = new HTTPBuilder( urlPrefix )
	}
	
	public Object create(_binding) {
		def template = new File("conf/${fileName}.json").text
		
		//Replace template values
		def engine = new GStringTemplateEngine()
		if(_binding) bindings << _binding
		def entity = engine.createTemplate(template).make(bindings).toString()

		println "${urlPrefix}${url} \n"
		this.util.printAsCode(entity)
		http.request( POST, JSON )  {
			uri.path           = "${url}"
			headers.'X-ApiKey' = key
			body               = entity
			requestContentType = JSON

			response.success = { resp, json ->
				println "**Response** \n"
				println "STATUS: ${resp.statusLine} \n"
				if (resp.headers.Location){
					def splitter = "${url}/"
					println "${resp.headers.Location} \n"
					def id = resp.headers.Location.split(splitter)[1]
					new File("id").write(id)
				} else{ //Searching
					this.util.printAsCode(new JsonBuilder(json).toPrettyString())
					return json
				}
				return true
			}

			response.failure = { resp->
				println "STATUS: ${resp.statusLine} \n"
				resp.headers.each { h -> println " ${h.name} : ${h.value}" }
				return false
			}
		}
	}

	def read(String id){

		if(!id){
			id = new File("id").text
		}

		println "${urlPrefix}${url}/${id} \n"

		http.request( GET )  {
			uri.path           = "${url}/${id}"
			headers.'X-ApiKey' = key

			response.success = { resp, json ->
				println "**Response** \n"
				println "STATUS: ${resp.statusLine} \n"
				this.util.printAsCode (new JsonBuilder(json).toPrettyString())
				return json
			}

			response.failure = { resp ->
				println "STATUS: ${resp.statusLine} \n"
				resp.headers.each { h -> println " ${h.name} : ${h.value}" }
				return false
			}
		}
	}

	def update(String id){

		if(!id){
			id = new File("id").text
		}

		def template = new File("conf/update_${fileName}.json").text
		
		//Replace template values
		def engine = new GStringTemplateEngine()
		def entity = engine.createTemplate(template).make(bindings).toString()

		println "${urlPrefix}${url}/${id} \n"
		
		http.request( PUT, JSON )  {
			uri.path           = "${url}/${id}"
			body               = entity
			headers.'X-ApiKey' = key
			requestContentType = JSON

			response.success = { resp ->
				println "STATUS: ${resp.statusLine} \n"
				return true
			}

			response.failure = { resp ->
				println "STATUS: ${resp.statusLine} \n"
				resp.headers.each { h -> println " ${h.name} : ${h.value}" }
				return false
			}
		}
	}

	def delete(String id){

		if(!id){
			id = new File("id").text
		}

		println "${urlPrefix}${url}/${id} \n"
		
		http.request( DELETE )  {
			uri.path           = "${url}/${id}"
			headers.'X-ApiKey' = key

			response.success = { resp ->
				println "STATUS: ${resp.statusLine} \n"
				return true
			}

			response.failure = { resp ->
				println "STATUS: ${resp.statusLine} \n"
				resp.headers.each { h -> println " ${h.name} : ${h.value}" }
				return false
			}
		}
	}
	

}
