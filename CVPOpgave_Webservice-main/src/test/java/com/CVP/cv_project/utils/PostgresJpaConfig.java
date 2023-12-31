package com.CVP.cv_project.utils;

import com.CVP.cv_project.repos.RoleRepository;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@TestConfiguration
@PropertySource("testApplication.properties")
public class PostgresJpaConfig {

    private String USERNAME = System.getenv("USERTESTERCV");
    private String PW = System.getenv("PWTESTERCV");

    //@Bean
    public DriverManagerDataSource getDataSource(){return new DriverManagerDataSource("jdbc:postgresql://localhost:5432/testercv2",USERNAME,PW);}


}
