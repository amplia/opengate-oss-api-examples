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
	key = "ad7bfb70-69df-45de-bcb9-36e1e35fb629"
}