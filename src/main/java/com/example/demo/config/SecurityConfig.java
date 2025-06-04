package com.example.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.example.demo.ss.XAbstractUserDetailsAuthenticationProvider;
import com.example.demo.ss.XBasicAuthenticationFilter;
import com.example.demo.ss.XUserDetailService;

/**
 * The real deal!
 */
@Profile("!dev")
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableAspectJAutoProxy
public class SecurityConfig {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Give spring context our {@link XAbstractUserDetailsAuthenticationProvider} and it will override the default {@link AuthenticationProvider}
     * @return Our implementiation of a {@link AuthenticationProvider}
     */
    @Bean
    public AuthenticationProvider getAuthenticationProvider() {
        return new XAbstractUserDetailsAuthenticationProvider(new XUserDetailService());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, final AuthenticationManagerBuilder auth,
            final AuthenticationConfiguration authenticationConfiguration) throws Exception {

        // Disable CSRF to allow POST without authorization
        // http.csrf(AbstractHttpConfigurer::disable);
        // Disable form login
        // http.formLogin(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(request -> request.requestMatchers("/error", "/test.html", "/favicon.ico","/logout").permitAll());
        // Enable authentication filter for all request
        // http.authorizeHttpRequests(request -> request.anyRequest().authenticated());

        http.authorizeHttpRequests(request -> request.requestMatchers("/hello/**").hasAnyRole("ADMIN"));
        
        // logger.error("AHAHA {}", authenticationConfiguration);
        // logger.error("AHAHA2 {}", authenticationConfiguration.getAuthenticationManager());

        // Replace BasicAuthenticationFilter with ours
        http.addFilterAt(new XBasicAuthenticationFilter(authenticationConfiguration.getAuthenticationManager(),
                new BasicAuthenticationEntryPoint()), BasicAuthenticationFilter.class);

        return http.build();
    }
}
