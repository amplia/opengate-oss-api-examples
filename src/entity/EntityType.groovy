package entity

enum EntityType {
	DEVICE("device"),
	SUBSCRIBER("subscriber"),
	SUBSCRIPTION("subscription"),
	COMM_MODULE("commModule")

	private type

	private EntityType(String type){
		this.type = type
	}

	String toString(){
		return type
	}
}