#!/usr/bin/env %_GROOVY_HOME_%/bin/groovy

import groovy.sql.Sql

def cli = new CliBuilder(usage: 'user -[cdh] [args]')
// Create the list of options.
cli.with {
    c longOpt: 'create', args : 1, argName : 'user', 'Create new user in Radius'
    d longOpt: 'delete', args : 1, argName : 'name', 'Delete user in Radius'
    h longOpt: 'help'  , 'Show usage information'
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
    def userConfig = new ConfigSlurper().parse(new File(options.c).toURL())

    def username    = userConfig.user.username
    def password    = userConfig.user.password
    def msisdn      = userConfig.user.callingStationId
    def ip          = userConfig.user.framedIpAddress
    def mask        = userConfig.user.framedIpNetmask

    println "Creating new user: ${username}, ${password}, ${msisdn}, ${ip}, ${mask}"
    def select     = 'select * from radcheck where username = ?'
    def statement1 = 'insert into RADCHECK (ID, USERNAME, ATTRIBUTE, OP, VALUE) values ( RADCHECK_SEQ.nextval, ?, ?, ?, ?)'
    def statement2 = 'insert into RADREPLY (ID, USERNAME, ATTRIBUTE, OP, VALUE) values ( RADREPLY_SEQ.nextval, ?, ?, ?, ?)'

    def sql = getSqlInstance()
    def result =sql.firstRow(select, [username])
    if(!result){
        sql.execute(statement1, [ username, "User-Name", "==", username])
        sql.execute(statement1, [ username, "Password", "==", password])
        sql.execute(statement1, [ username, "Calling-Station-Id", "==", msisdn])
        sql.execute(statement2, [ username, "Framed-IP-Address", "==", ip])
        sql.execute(statement2, [ username, "Framed-IP-Netmask", "==", mask])
        println "User created"
    }
    
    return
}

if (options.d) {
    println "Deleting user ${options.d}"
    def sql = getSqlInstance()
    sql.execute("delete from radcheck where username = ?" , [options.d])
    println "${sql.updateCount} row/s affected"

    sql.execute("delete from radreply where username = ?" , [options.d])
    println "${sql.updateCount} row/s affected"
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