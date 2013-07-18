package entity

import javax.print.attribute.standard.Chromaticity;

class TestSuite {
	
	private static final int TIMEOUT= 120000

	def crudForEntityTest(EntityType type){
		def tester = new CrudTester(ServiceUrl.PROVISION, type)
		tester.create(null)
		println "---------------------"
		tester.read()
		println "---------------------"
		tester.update()
		println "---------------------"
		tester.read()
		println "---------------------"
		tester.delete()
		println "---------------------"
		tester.read()
	}
	
	def crForOperationTest(){
		//Operation target entity
		def crudEntityTester = new CrudTester(ServiceUrl.PROVISION, EntityType.DEVICE)
		crudEntityTester.create(null)
		println "---------------------"
		def assetId = new File("id").text
		try{
		def crudOperationTester = new CrudTester("operations","request_measure")
		def binding=[uuid:assetId]
		crudOperationTester.create(binding)
		println "---------------------"
		
		waitingForResponse(crudOperationTester)
		}finally{
		//Delete entity
		crudEntityTester.delete(assetId)
		}
	}
		
	
	def waitingForResponse(CrudTester crudTester){
		def response = false
		def count=0
		def operationId = new File("id").text
		def jsonResponse;
		
		def startTime = System.currentTimeMillis()
		while (isInTime(startTime) && !response){
			count ++;
			sleep(5000)
			println "Attempt ${count}"
			jsonResponse = crudTester.read(operationId)
			println "pendingExecution = ${jsonResponse.operations.summary.pendingExecution}"
			if (jsonResponse.operations.summary.pendingExecution==0){
				response=true
			}
 		}

		if(!response)
			println "Timeout!!!"
		else
			println "Response Received"
	}
	
	private boolean isInTime(long startTime){
		(System.currentTimeMillis() - startTime ) < TIMEOUT
	}
	
	static main(args){

		def suite = new TestSuite()
		// Entity tests
		EntityType.values().each { entityType ->
			println "CRUD for ${entityType}"
			suite.crudForEntityTest(entityType)
			println "***************************************"
		}
		
		//Operations tests
		suite.crForOperationTest()
	}
}
