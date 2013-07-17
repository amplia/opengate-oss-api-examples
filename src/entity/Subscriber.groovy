package entity

class Subscriber extends Crud{

	Subscriber(){
		entityName= "subscriber"
	}

	static main(args){
		def subscriber = new Subscriber()
		subscriber.create()
		subscriber.read()
		subscriber.update()
		subscriber.read()
		subscriber.delete()
		subscriber.read()
		
	}
}
