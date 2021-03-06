package ru.testservice.serviceapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.testservice.serviceapp.interceptors.MoreCoursesInterceptor;
import ru.testservice.serviceapp.service.CourseService;
import ru.testservice.serviceapp.service.IStorageService;

import java.util.concurrent.TimeUnit;


@EnableWebMvc
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private CourseService cs;
    @Autowired
    private IStorageService IStorageService;
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/" };

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS)
        .setCacheControl(CacheControl.maxAge(24, TimeUnit.HOURS).cachePublic());
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MoreCoursesInterceptor(cs, IStorageService));
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter(){
        return new HiddenHttpMethodFilter();
    }
}
