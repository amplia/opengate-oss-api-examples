package entity

class Util {

	def printAsCode(String text){
		text.eachLine { it ->
			println it.padLeft(5)
		}
		println"\n"
	}
}
