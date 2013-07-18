environments {
	amplia {
		dataSource {
			driverClassName = "oracle.jdbc.OracleDriver"
			ip              = "%_ORACLE_IP_%"
			port            = 1521
			url             = "jdbc:oracle:thin:@%_ORACLE_IP_%:%_ORACLE_PORT_%:%_ORACLE_SID_%"
			username        = "%_ORACLE_LOGIN_%"
			password        = "%_ORACLE_PASSWORD_%"
		}
		
		api {
			ip = "192.168.1.22"
			port = "25281"
			url = "http://${ip}:${port}/"
			//key = "ad7bfb70-69df-45de-bcb9-36e1e35fb629"
			key = "decb91d3-775a-2b65-e040-0f0ab8690a5f" //UUID amplia)))
		}
		
		uuids {
			softwareUuid = "d93907d1-bba1-4f1f-9414-2c4007a6ae4a"
			firmwareUuid = "dac59a98-83fd-434e-be3e-69e942a07387"
			hardwareUuid = "c5d8c589-9f49-4989-a271-d60aa3f15d3b"
		}		
	}
	
	neoris {
		dataSource {
			driverClassName = "oracle.jdbc.OracleDriver"
			ip              = "%_ORACLE_IP_%"
			port            = 1521
			url             = "jdbc:oracle:thin:@%_ORACLE_IP_%:%_ORACLE_PORT_%:%_ORACLE_SID_%"
			username        = "%_ORACLE_LOGIN_%"
			password        = "%_ORACLE_PASSWORD_%"
		}
		
		api {
			ip = "10.15.105.230"
			port = "25281"
			url = "http://${ip}:${port}/"
			//key = "ad7bfb70-69df-45de-bcb9-36e1e35fb629"
			key = "decb91d3-775a-2b65-e040-0f0ab8690a5f" //UUID amplia)))
		}

		uuids {
			softwareUuid = "1c30bc1a-4dfd-4d33-9d06-66b78ce1484e"
			firmwareUuid = "385fa018-b958-467d-8367-784eed5ba22f"
			hardwareUuid = "407ac4fd-dbd6-41fe-b4d3-0cd800e27274"
		}		
	}
}