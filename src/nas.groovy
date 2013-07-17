#!/usr/bin/env %_GROOVY_HOME_%/bin/groovy

import groovy.sql.Sql

def cli = new CliBuilder(usage: 'nas -[cdhr] [args]')
// Create the list of options.
cli.with {
	c longOpt: 'create', args : 1, argName : 'config', 'Create new NAS'
	d longOpt: 'delete', args : 1, argName : 'name', 'Delete NAS'
	h longOpt: 'help'  , 'Show usage information'
	p longOpt: 'ping'  , 'Ping database server'
	r longOpt: 'read'  , 'Read all NASes'
}

if (args.size() < 1) {
	cli.usage()
	return
}

def options = cli.parse(args)

if (!options) {
	return
}

if (options.h) {
	cli.usage()
	return
}


if (options.c) {
	def config = new ConfigSlurper().parse(new File(options.c).toURL())

	def nasname     = config.nas.nasname
	def shortname   = config.nas.shortname
	def type        = config.nas.type
	def ports       = config.nas.ports
	def secret      = config.nas.secret
	def community   = config.nas.community
	def description = config.nas.description

	println "Creating new NAS: ${nasname}, ${shortname}, ${type}, ${ports}, ${secret}, ${community}, ${description}"
	def statement = 'insert into NAS (ID, NASNAME, SHORTNAME, TYPE, PORTS, SECRET, COMMUNITY, DESCRIPTION) values ( NAS_SEQ.nextval, ?, ?, ?, ?, ?, ?, ?)'
	def sql = getSqlInstance()
	sql.execute(statement, [
		nasname,
		shortname,
		type,
		ports,
		secret,
		community,
		description
	])
	println "NAS created"
	return
}

if (options.d) {
	println "Deleting NAS ${options.d}"
	def sql = getSqlInstance()
	sql.execute("delete from nas where nasname = ?" , [options.d])
	println "${sql.updateCount} row/s affected"
	return
}

if (options.p) {
	def sql = getSqlInstance()
	println 'Server is alive'
	return
}

if (options.r) {
	def sql = getSqlInstance()
	sql.eachRow( 'select * from nas' ) { println "$it.id -- ${it.nasname} --" }
	return
}


def getSqlInstance(){

	def config = new ConfigSlurper().parse(new File('config.groovy').toURL())

	def driverClassName = config.dataSource.driverClassName
	def ip              = config.dataSource.ip
	def url             = config.dataSource.url
	def username        = config.dataSource.username
	def password        = config.dataSource.password

	println "Connecting to server ${ip} with credentials ${username}/${password} through ${driverClassName}"

	def sql
	try {
		sql = Sql.newInstance( url, username, password, driverClassName )
	}
	catch(all) {
		println "Can't connect ${all.message}"
	}

	if (sql) {
		sql
	} else {
		System.exit(1)
	}

}