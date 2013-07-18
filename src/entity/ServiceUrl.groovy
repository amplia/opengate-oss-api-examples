package entity

enum ServiceUrl {

	PROVISION("provision"),
	OPERATION("operations"),
	SEARCH_PROVISION("search/provision"),
	SEARCH_COLLECTION("search/collection")

	private url

	private ServiceUrl(String url){
		this.url = url
	}

	String toString(){
		return url
	}
}
