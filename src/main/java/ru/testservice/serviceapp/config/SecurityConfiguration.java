package ru.testservice.serviceapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    public Environment environment;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
//                .antMatchers("/splash").permitAll()
//                .antMatchers("/", "/tests/**", "/ticket/**","/docsupload/").authenticated()
                .antMatchers("/**").permitAll()
                .and()
                .formLogin()
                .loginPage("/splash")
                .defaultSuccessUrl("/")
                .usernameParameter("j_login")
                .passwordParameter("j_pass")
                .failureUrl("/splash?error=true")
                .permitAll()
                .and()
                .rememberMe().key("sjha876sdhu))")
                .and().
                logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/splash")

        ;
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        String adminPassword = environment.getProperty("adminPassword");
        UserDetails admin =
                User.withDefaultPasswordEncoder()
                        .username("admin")
                        .password(adminPassword)
                        .roles("ADMIN")
                        .build();

        return new InMemoryUserDetailsManager(admin);
    }
}
