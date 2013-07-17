package entity

class Device extends Crud{

	Device(){
		entityName= "device"
	}

	static main(args){
		def device = new Device()
		device.create()
		device.read()
		device.update()
		device.read()
		device.delete()
		device.read()
		
	}
}
