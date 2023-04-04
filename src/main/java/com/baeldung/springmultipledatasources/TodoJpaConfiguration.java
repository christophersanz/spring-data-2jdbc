package com.baeldung.springmultipledatasources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
//@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.baeldung.springmultipledatasources.repository.todos",
        entityManagerFactoryRef = "todosEntityManagerFactory",
        transactionManagerRef = "todosTransactionManager"
)
@Profile("!tc")
public class TodoJpaConfiguration {

    public TodoJpaConfiguration() {
        super();
    }

    @Autowired
    private Environment env;

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean todosEntityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(todosDataSource());
        em.setPackagesToScan("com.baeldung.springmultipledatasources.model.todos");

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        final HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Primary
    @Bean
    public DataSource todosDataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(env.getProperty("spring.datasource.todos.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.todos.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.todos.password"));
        dataSource.setDriverClassName(env.getProperty("spring.datasource.todos.driverClassName"));

        return dataSource;
    }

    @Primary
    @Bean
    public PlatformTransactionManager todosTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(todosEntityManagerFactory().getObject());
        return transactionManager;
    }
}