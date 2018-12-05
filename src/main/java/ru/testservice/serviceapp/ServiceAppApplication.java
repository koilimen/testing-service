package ru.testservice.serviceapp;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import javax.sql.DataSource;

@SpringBootApplication
@PropertySources({
        @PropertySource(value = "file:/opt/config/prombez-web.properties"),
        @PropertySource(value = "classpath:/application.properties"),
        @PropertySource(value = "classpath:/application-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
} )
public class ServiceAppApplication extends SpringBootServletInitializer{



	public static void main(String[] args) {
		SpringApplication.run(ServiceAppApplication.class, args);
	}
}
