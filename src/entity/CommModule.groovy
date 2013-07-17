package entity

class CommModule extends Crud{

	CommModule(){
		entityName= "commModule"
	}

	static main(args){
		def commModule = new CommModule()
		commModule.create()
/*		commModule.read()
		commModule.update()
		commModule.read()
		commModule.delete()
		commModule.read()*/
	}
}

