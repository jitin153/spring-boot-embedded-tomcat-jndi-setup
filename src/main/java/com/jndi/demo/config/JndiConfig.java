package com.jndi.demo.config;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.catalina.Context;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiObjectFactoryBean;

/*
 * Reference: https://github.com/wilkinsona/spring-boot-sample-tomcat-jndi
 */

@Configuration
public class JndiConfig {

	@Value("${jndi.datasource.driver-class-name}")
	private String driverClassName;
	
	@Value("${jndi.datasource.url}")
	private String dataSourceUrl;
	
	@Value("${jndi.datasource.username}")
	private String username;
	
	@Value("${jndi.datasource.password}")
	private String password;
	
	@Bean
	public TomcatServletWebServerFactory tomcatFactory() {
		return new TomcatServletWebServerFactory() {
			@Override
			protected TomcatWebServer getTomcatWebServer(org.apache.catalina.startup.Tomcat tomcat) {
				tomcat.enableNaming();
				return super.getTomcatWebServer(tomcat);
			}

			@Override
			protected void postProcessContext(Context context) {
				ContextResource resource = new ContextResource();
				resource.setName("jdbc/myJndiDataSOurce");
				resource.setType(DataSource.class.getName());
				resource.setProperty("driverClassName", driverClassName);

				resource.setProperty("url", dataSourceUrl);
				resource.setProperty("username", username);
				resource.setProperty("password", password);
				context.getNamingResources().addResource(resource);
			}
		};
	}

	@Bean(destroyMethod = "")
	public DataSource jndiDataSource() throws IllegalArgumentException, NamingException {
		JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
		bean.setJndiName("java:comp/env/jdbc/myJndiDataSOurce");
		bean.setProxyInterface(DataSource.class);
		bean.setLookupOnStartup(false);
		bean.afterPropertiesSet();
		return (DataSource) bean.getObject();
	}
}
