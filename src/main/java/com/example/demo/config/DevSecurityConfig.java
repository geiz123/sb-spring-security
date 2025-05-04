package com.example.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Allow all access
 */
@Profile("dev")
@Configuration 
public class DevSecurityConfig {

    Logger logger = LoggerFactory.getLogger(DevSecurityConfig.class);

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logger.info("{}(((((((((()))))))))){}Allow all access{}(((((((((())))))))))", System.lineSeparator(),
                System.lineSeparator(), System.lineSeparator());

        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}
