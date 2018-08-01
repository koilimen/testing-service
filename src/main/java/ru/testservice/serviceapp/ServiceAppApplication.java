package ru.testservice.serviceapp;

import com.zaxxer.hikari.HikariDataSource;
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
        @PropertySource(value = "classpath:/application.properties"),
        @PropertySource(value = "classpath:/application-${spring.profiles.active}.properties")
} )
public class ServiceAppApplication extends SpringBootServletInitializer{

//	@Bean
//	@ConfigurationProperties(prefix="app.datasource")
//	public DataSource dataSource(){
//        return DataSourceBuilder.create().type(HikariDataSource.class).build();
//	}

	public static void main(String[] args) {
		SpringApplication.run(ServiceAppApplication.class, args);
	}
}
