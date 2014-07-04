package com.my.payment.DAOtests;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "com.my.payment.dao")
@PropertySource("classpath:jdbc.properties")
@EnableTransactionManagement
public class DAOsConfig {
	@Autowired
	private Environment environment;
	///Datasource bean
    @Bean
    public DriverManagerDataSource driverManagerDataSource() {
    	DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
    	driverManagerDataSource.setDriverClassName(environment.getProperty("jdbc.driverClassName"));
    	driverManagerDataSource.setUrl(environment.getProperty("jdbc.databaseurl"));
    	driverManagerDataSource.setUsername(environment.getProperty("jdbc.username"));
    	driverManagerDataSource.setPassword(environment.getProperty("jdbc.password"));
    	return driverManagerDataSource;
    }	
  ///Hibernate session factory bean
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setPackagesToScan("com.my.payment.model");
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto","create");
        properties.setProperty("hibernate.dialect",environment.getProperty("jdbc.dialect"));
        properties.setProperty("hibernate.connection.charSet","UTF-8");
        sessionFactory.setHibernateProperties(properties);
        sessionFactory.setDataSource(driverManagerDataSource());
        return sessionFactory;
    }
  ///Transaction manager bean
    @Bean
    public HibernateTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager  hibernateTransactionManager = new HibernateTransactionManager();
        hibernateTransactionManager.setSessionFactory(sessionFactory().getObject());
        return  hibernateTransactionManager;
    }
}
