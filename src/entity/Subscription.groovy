package entity

class Subscription extends Crud{

	Subscription(){
		entityName= "subscription"
	}

	static main(args){
		def subscription = new Subscription()
		subscription.create()
		subscription.read()
		subscription.update()
		subscription.read()
		subscription.delete()
		subscription.read()
	}
}