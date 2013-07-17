#!/usr/bin/env %_GROOVY_HOME_%/bin/groovy

import groovy.sql.Sql
import groovy.json.*
import static groovyx.net.http.ContentType.*
import groovyx.net.http.*
import static groovyx.net.http.Method.*

def cli = new CliBuilder(usage: 'nas -[cudhr] [args]')
// Create the list of options.
cli.with {
    c longOpt: 'create' , args : 1, argName : 'nas'                                      , 'Create new NAS'
    u longOpt: 'update' , args : 2, argName : 'nas:nasName', valueSeparator: ':' as char , 'Update nas nasName'
    d longOpt: 'delete' , args : 1, argName : 'name'                                     , 'Delete NAS'
    h longOpt: 'help'                                                                    , 'Show usage information'
    r longOpt: 'read'                                                                    , 'Read all NASes'
}

if (args.size() < 1) {
    cli.usage()
    return
}

def options = cli.parse(args)

if (!options) {
    return
} 

if (options.h) {
    cli.usage()
    return
}


if (options.c) {
    def config = new ConfigSlurper().parse(new File(options.c).toURL())

    def name     = config.nas.nasname
    def sname    = config.nas.shortname
    def typeNas  = config.nas.type
    def portsNas = config.nas.ports
    def password = config.nas.secret
    def comm     = config.nas.community
    def desc     = config.nas.description

    println "Creating new NAS: ${name}, ${sname}, ${typeNas}, ${portsNas}, ${password}, ${comm}, ${desc}"

    def builder = new JsonBuilder()

    try {
    
        builder  {
            nasname name
            shortname sname
            type typeNas
            ports portsNas
            secret password
            community comm
            description desc
        }

        println builder.toPrettyString() 

        def nas = new HTTPBuilder( 'http://localhost:8080/opengateradius-api/' )

        nas.request( POST, JSON )  {
            uri.path           = 'nas'
            body               = builder.toString()
            requestContentType = JSON

            response.success = { resp ->
                println resp.statusLine
                return
            }

            response.failure = { resp, json ->
                println resp.statusLine
                println new JsonBuilder(json).toPrettyString()
                return
            }
        }
    }
    catch(Exception e) {
        println "${e.message}"
        return 
    }
}

if (options.u) {
	
	def config = new ConfigSlurper().parse(new File(options.u).toURL())

    def name     = config.nas.nasname
    def sname    = config.nas.shortname
    def typeNas  = config.nas.type
    def portsNas = config.nas.ports
    def password = config.nas.secret
    def comm     = config.nas.community
    def desc     = config.nas.description
    
    config = new ConfigSlurper().parse(new File('config.groovy').toURL())
    def url    = config.radius.url

    def nasName = options.us[1]

    println "Updating NAS: ${nasName}"

    def builder = new JsonBuilder()

    try {
    
        builder  {
            nasname name
            shortname sname
            type typeNas
            ports portsNas
            secret password
            community comm
            description desc
        }
        

        println builder.toPrettyString() 

        def nas = new HTTPBuilder( url )

        nas.request( PUT, JSON )  {
            uri.path           = "nas/${nasName}"
            body               = builder.toString()
            requestContentType = JSON

            response.success = { resp ->
                println resp.statusLine
                return
            }

            response.failure = { resp, json ->
                println resp.statusLine
                println new JsonBuilder(json).toPrettyString()
                return
            }
        }
    }
    catch(Exception e) {
        println "${e.message}"
        return 
    }
}

if (options.d) {

    def config = new ConfigSlurper().parse(new File(options.d).toURL())
    def nasname = config.nas.nasname
    
    config = new ConfigSlurper().parse(new File('config.groovy').toURL())
    def url    = config.radius.url

    println "Deleting NAS ${nasname}"

    def nas = new HTTPBuilder(url)

    try{

        nas.request( DELETE)  {
            uri.path           = "nas/${nasname}"

            response.success = { resp ->
                println resp.statusLine
                println "${nasname} deleted"

                return 
            }

            response.failure = { resp, json ->
                println resp.statusLine
                println new JsonBuilder(json).toPrettyString()
                return
            }
        }
    }
    catch(Exception e){
        println "${e.message}"
        return
    }
}

if (options.r) {

	def config = new ConfigSlurper().parse(new File('config.groovy').toURL())
    def url    = config.radius.url

    def radius = new HTTPBuilder(url)

    try{

        radius.request( GET)  {
            uri.path           = "nas"

            response.success = { resp, json ->
                println resp.statusLine
                println new JsonBuilder(json).toPrettyString()
                return 
            }

            response.failure = { resp, json ->
                println resp.statusLine
                println new JsonBuilder(json).toPrettyString()
                return
            }
        }
    }
    catch(Exception e){
        println "${e.message}"
        return
    }
}


def getSqlInstance(){
    
    def config = new ConfigSlurper().parse(new File('config.groovy').toURL())

    def driverClassName = config.dataSource.driverClassName
    def ip              = config.dataSource.ip
    def url             = config.dataSource.url
    def username        = config.dataSource.username
    def password        = config.dataSource.password

    println "Connecting to server ${ip} with credentials ${username}/${password} through ${driverClassName}"

    def sql
    try {
        sql = Sql.newInstance( url, username, password, driverClassName )
    }
    catch(all) {
        println "Can't connect ${all.message}"
    }
    
    if (sql) {        
        sql
    } else {
        System.exit(1)
    }

}