package entity

enum OperationType {

	EQUIPMENT_DIAGNOSTIC("equipment_diagnostic"),
	FACTORY_RESET("factory_reset"),	
	POWER_OFF_EQUIPMENT("power_off_equipment"),
	POWER_ON_EQUIPMENT("power_on_equipment"),
	REBOOT_EQUIPMENT("reboot_equipment"),
	REFRESH_INFO("refresh_info"),
	REFRESH_LOCATION("refresh_location"),
	REFRESH_PRESENCE("refresh_presence"),
	REQUEST_MEASURE("request_measure"),
	RESET_COMMUNICATIONS("reset_communications"),
	SET_CLOCK_EQUIPMENT("set_clock_equipment"),
	SET_LOCATION("set_location"),
	SHUT_DOWN_COMMUNICATIONS("shut_down_communications"),
	START_SOFTWARE("start_software"),
	STATUS_DIAGNOSTIC("status_diagnostic"),
	STOP_SOFTWARE("stop_software"),
	//UPDATE("update"), // Necesita un parametro mas obligatorio, el bundle
	WAKE_UP_COMMUNICATIONS("wake_up_communications")
	
	private operation

	private OperationType(String operation){
		this.operation = operation
	}

	String toString(){
		return operation
	}
}
