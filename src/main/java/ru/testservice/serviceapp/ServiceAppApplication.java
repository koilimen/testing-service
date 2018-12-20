package ru.testservice.serviceapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@PropertySources({
        @PropertySource(value = "file:/opt/config/prombez-web.properties"),
        @PropertySource(value = "classpath:/application.properties"),
        @PropertySource(value = "classpath:/application-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
} )
@EnableScheduling
public class ServiceAppApplication extends SpringBootServletInitializer{



	public static void main(String[] args) {
		SpringApplication.run(ServiceAppApplication.class, args);
	}
}
