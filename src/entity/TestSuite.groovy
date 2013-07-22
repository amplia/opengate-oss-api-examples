package entity

import javax.print.attribute.standard.Chromaticity;

class TestSuite {
	
	/* CONFIGURATION */
	//Timeout for operations
	private static final int TIMEOUT= 30000

	def crudForEntityTest(EntityType type){
		def tester = new CrudTester(ServiceUrl.PROVISION, type)
		println "#### POST ${type} \n"
		tester.create(null)
		println "#### GET ${type} \n"
		tester.read()
		println "#### UPDATE ${type} \n"
		tester.update()
		println "#### GET ${type} \n"
		tester.read()
		println "#### DELETE ${type} \n"
		tester.delete()
		println "#### GET ${type} \n"
		tester.read()
	}
	
	def crForOperationTest(String operationName){
		//Operation target entity
		def crudEntityTester = new CrudTester(ServiceUrl.PROVISION, EntityType.DEVICE)
		println "#### POST ${EntityType.DEVICE} for operations\n"
		crudEntityTester.create(null)
		
		def assetId = new File("id").text
		try{
		def crudOperationTester = new CrudTester("operations","operation_template")
		def binding=[uuid:assetId,operationType:operationName.toUpperCase()]
		println "#### Solicitud de operación: ${operationName} \n"
		crudOperationTester.create(binding)
		def operationId = new File("id").text
		waitingForOperationResponse(crudOperationTester, operationId)
		}finally{
		//Delete entity
		crudEntityTester.delete(assetId)
		}
	}
	
	def waitingForOperationResponse(CrudTester crudTester, String operationId){
		def response = false
		def count=0
		def jsonResponse;
		
		def startTime = System.currentTimeMillis()
		while (isInTime(startTime) && !response){
			count ++;
			sleep(5000)
			println "Attempt ${count} \n"
			jsonResponse = crudTester.read(operationId)
			if(jsonResponse){
				println "Pending Execution = ${jsonResponse.operations.summary.pendingExecution} \n"
				if (jsonResponse.operations.summary.pendingExecution==0){
					response=true
				}
			}
 		}

		if(!response)
			println "Timeout!!! \n"
		else
			println "Response Received \n"
	}
	
	private boolean isInTime(long startTime){
		(System.currentTimeMillis() - startTime ) < TIMEOUT
	}
	
	def cForSearchingTest(){
		println "\n### Búsquedas de datos recolectados \n"
		EntityType.values().each { entityType ->
			println "#### POST búsqueda de ${entityType} \n"
			cForSearchingTestAux(ServiceUrl.SEARCH_COLLECTION,entityType,null)
		}

		println "\n### Búsquedas de datos aprovisionados \n"
		def operations=["eq","neq","like"]
		def binding=[:]
		EntityType.values().each { entityType ->
			switch(entityType){
				case EntityType.DEVICE:
					binding = [operationId:"",propertyToFilter:"prov.serialNumber",dataToFilter:"SN34669000048"]
					break
				case EntityType.SUBSCRIBER:
					binding = [operationId:"",propertyToFilter:"prov.mainpath",dataToFilter:"/SN34669000037/352234669000037/8934070134669000037"]
					break
				case EntityType.SUBSCRIPTION:
					binding = [operationId:"",propertyToFilter:"prov.msisdn",dataToFilter:"34669000027"]
					break
				case EntityType.COMM_MODULE:
					binding = [operationId:"",propertyToFilter:"prov.entityName",dataToFilter:"GSM_669000043"]
					break
			}
			println "#### POST búsqueda de ${entityType} \n"
			def counter = new FilterCounterComparer()
			println "##### Filtrado vacío \n"
			def jsonResponse=cForSearchingTestAux(ServiceUrl.SEARCH_PROVISION, entityType, null)
			counter.saveInfo(FilterCounterComparer.EMPTY_FILTER, jsonResponse.page.of)
			
			operations.each{ operation ->
				println "##### Filtrado con operando: ${operation} \n"
				binding.put("operationId",operation)
				jsonResponse=cForSearchingTestAux(ServiceUrl.SEARCH_PROVISION, entityType, binding)
				counter.saveInfo(operation, jsonResponse.page.of)
			}
			println counter
		}
	}
	
	private cForSearchingTestAux(ServiceUrl serviceUrl, EntityType entityType, Map binding){
		def url = serviceUrl.toString()+"/"+entityType.toString()+"s";
		def crudEntityTester
		
		if(binding){//Solo usamos la plantilla si hay datos de binding (originalmente no habia datos recolectados)
			crudEntityTester = new CrudTester(url, "filterTemplate")
		}else{
			crudEntityTester = new CrudTester(url, "filterEmpty")
		}
		def jsonResponse = crudEntityTester.create(binding)
		
		return jsonResponse
	}
	
	static main(args){
		println "Inicio de la ejecución: ${new Date()} \n\n"
		println "\n# Resultados de las pruebas del OSS-API v6\n"
		println "\n## Introducción\n"
		println "* **Nota:** Los dispositivos que se dan de alta son siempre de tipo ASSET"
		def suite = new TestSuite()
		
		// ***** Entity tests *****
		println "\n## Pruebas del CRUD de Entidades\n"
		EntityType.values().each { entityType ->
			println "\n### CRUD de ${entityType}"
			suite.crudForEntityTest(entityType)
		}
		
		//***** Operations tests *****
		println "\n## Pruebas de operaciones \n"
		OperationType.values().each { operationType ->
			println "\n### Operación: ${operationType} \n"
			suite.crForOperationTest(operationType.toString())
		}
		
		//***** Searching *****
		println "\n## Pruebas de búsquedas\n"
		suite.cForSearchingTest()

		println "Fin de la ejecución: ${new Date()} \n\n"
	}
}
