package com.jndi.demo.controller;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JndiTestController {
	
	@Autowired
	private DataSource dataSource;

	@RequestMapping("/datasource")
	public String getDatasource() {
		return "DataSource retrieved from JNDI using JndiObjectFactoryBean: " + dataSource;
	}

	@RequestMapping("/dsfromjndi")
	public String getDatasourceFromJndi() throws NamingException {
		return "DataSource retrieved from JNDI: " +
				new InitialContext().lookup("java:comp/env/jdbc/myJndiDataSOurce");
	}
}
