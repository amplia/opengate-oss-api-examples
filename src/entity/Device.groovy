package entity
import net.sf.json.JSON
import groovy.json.JsonBuilder
import groovyx.net.http.HTTPBuilder


class Device extends Crud{

	Device(){
		entityName= "device"
	}

	static main(args){
		def device = new Device()
		device.create()
	}
}
