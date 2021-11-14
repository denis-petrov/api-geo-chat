package com.app.apigeochat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("/**"));
        corsConfiguration.setAllowedMethods(List.of("*"));

        http.authorizeRequests().antMatchers("/").permitAll().and()
                .cors().configurationSource(request -> corsConfiguration).and()
                .csrf().disable();
    }
}
