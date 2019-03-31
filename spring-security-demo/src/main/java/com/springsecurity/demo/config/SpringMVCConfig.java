package com.springsecurity.demo.config;

import java.beans.PropertyVetoException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.springsecurity.demo")
@PropertySource("classpath:persistence-oracle.properties")
public class SpringMVCConfig {

	@Autowired
	Environment environment;

	// set up a logger for diagnostics
	private Logger logger = Logger.getLogger(getClass().getName());

	@Bean
	public DataSource securityDataSource() {

		// create connection pool
		ComboPooledDataSource dataSource = new ComboPooledDataSource();

		// set the jdbc driver
		try {
			dataSource.setDriverClass(environment.getProperty("jdbc.driver"));
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// log the connection props
		// for sanity's sake, log this info
		// just to make sure we are REALLY reading data from properties file

		logger.info(">>> jdbc.url=" + environment.getProperty("jdbc.url"));
		logger.info(">>> jdbc.user=" + environment.getProperty("jdbc.user"));

		dataSource.setJdbcUrl(environment.getProperty("jdbc.url"));
		dataSource.setUser(environment.getProperty("jdbc.user"));
		dataSource.setPassword(environment.getProperty("jdbc.password"));
		dataSource.setPreferredTestQuery(environment.getProperty("c3p0.preferredTestQuery"));
		dataSource.setTestConnectionOnCheckout(true);

		// set connection pool props

		dataSource.setInitialPoolSize(getIntProperty("connection.pool.initialPoolSize"));
		dataSource.setMinPoolSize(getIntProperty("connection.pool.minPoolSize"));
		dataSource.setMaxPoolSize(getIntProperty("connection.pool.maxPoolSize"));
		dataSource.setMaxIdleTime(getIntProperty("connection.pool.maxIdleTime"));

		return dataSource;
	}

	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/view/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	// need a helper method
	// read environment property and convert to int
	private int getIntProperty(String propName) {
		String propVal = environment.getProperty(propName);
		// now convert to int
		int intPropVal = Integer.parseInt(propVal);
		return intPropVal;
	}

}
