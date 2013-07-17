#!/usr/bin/env %_GROOVY_HOME_%/bin/groovy

import groovy.sql.Sql
import groovy.json.*
import static groovyx.net.http.ContentType.*
import groovyx.net.http.*
import static groovyx.net.http.Method.*

def cli = new CliBuilder(usage: 'test -[cdhstpnfea] [args]')
cli.with {
    ca longOpt: 'createAsset', args : 1, argName : 'asset' , 'Create new Asset'
	ua longOpt: 'updateAsset', args : 1, argName : 'asset' , 'Create new Asset'
	ga longOpt: 'getAsset'   , args : 1, argName : 'asset' , 'Get an Asset'
    da longOpt: 'deleteAsset', args : 1, argName : 'asset' , 'Delete an Asset'
    h longOpt: 'help'                                       , 'Show usage information'
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


if (options.ca) {
   createAsset(options.ca)
}

if (options.ua) {
	deleteAsset(options.ua)
}

if (options.ga) {
	getAsset(options.ga)
}

if (options.da) {
    Asset(options.da)
}
   

def createAsset(String assetFile){

    def data   = new ConfigSlurper().parse(new File(assetFile).toURL())
    
	def name   = data.user.username
    def pass   = data.user.password
    def msisdn = data.user.callingStationId
    def ip     = data.user.framedIpAddress
    def mask   = data.user.framedIpNetmask

	def customIdentifier = data.asset.provision.customId
		

    def config = new ConfigSlurper().parse(new File('config.groovy').toURL())
    def url    = config.radius.url


    def builder = new JsonBuilder()

    try {
    
		builder{
			asset {
				provision{
						 customId customIdentifier
						 template="default"
						 specificType=["CONCENTRATOR"]
						 name= ["CONCENTRATOR_TEST_DFG"]
						 description=["DFG device description"]
						 admin{
							 organization= "endesa_organization"
							 channel= "TELECONTROL"
							 administrativeState= "ACTIVE"
							 serviceGroup= "GPRS_TELECONTROL"
						 }
						 type= "gateway"
						 serialNumber=["SNDFG00000000"]
						 hardware=["407ac4fd-dbd6-41fe-b4d3-0cd800e27274"]
						 software= ["1c30bc1a-4dfd-4d33-9d06-66b78ce1484e", "385fa018-b958-467d-8367-784eed5ba22f"]
						 location= [
							 {
								 coordinates= {
									 latitude= 43.53722
									 longitude= -5.66759
								 }
								 postal= "33207"
							 }
						 ]
						 operationalStatus= ["UP"]
						 mainPath= [ "/SNDFG00000000"]
					 }
			 }
		}
		
        builder  {
            username name
            password pass
            callingStationId msisdn
            framedIpAddress ip
            framedIpNetmask mask
        }

        println builder.toPrettyString() 

        def radius = new HTTPBuilder( url )

        radius.request( POST, JSON )  {
            uri.path           = 'devices'
            body               = builder.toString()
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
    catch(Exception e) {
        println "${e.message}"
        return false
    }

}

def deleteAsset(String userFile){

    def data = new ConfigSlurper().parse(new File(userFile).toURL())
    def username = data.user.username

    def config = new ConfigSlurper().parse(new File('config.groovy').toURL())
    def url    = config.radius.url

    def radius = new HTTPBuilder(url)

    try{

        radius.request( DELETE, JSON)  {
            uri.path           = "users/${username}"

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
    catch(Exception e){
        println "${e.message}"
        return false
    }
}

