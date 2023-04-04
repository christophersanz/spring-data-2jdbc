package com.baeldung.springmultipledatasources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        basePackages = "com.baeldung.springmultipledatasources.repository.topics",
        entityManagerFactoryRef = "topicsEntityManagerFactory",
        transactionManagerRef = "topicsTransactionManager")
@Profile("!tc")
public class TopicJpaConfiguration {

    @Autowired
    private Environment env;

    public TopicJpaConfiguration() {
        super();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean topicsEntityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(topicsDataSource());
        em.setPackagesToScan("com.baeldung.springmultipledatasources.model.topics");

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        final HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public DataSource topicsDataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(env.getProperty("spring.datasource.topics.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.topics.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.topics.password="));
        dataSource.setDriverClassName(env.getProperty("spring.datasource.topics.driverClassName"));

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager topicsTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(topicsEntityManagerFactory().getObject());
        return transactionManager;
    }
}
