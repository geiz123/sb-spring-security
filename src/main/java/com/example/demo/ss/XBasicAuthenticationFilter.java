package com.example.demo.ss;

import java.io.IOException;

import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Create your own class if you want to redirect else where when exception
 * happens
 * https://howtodoinjava.com/spring-security/http-basic-authentication-example/
 */
public class XBasicAuthenticationFilter extends BasicAuthenticationFilter {

    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();
    private RememberMeServices rememberMeServices = new NullRememberMeServices();
    private boolean ignoreFailure = false;
    private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();
    private AuthenticationEntryPoint authenticationEntryPoint;

    public XBasicAuthenticationFilter(AuthenticationManager authenticationManager,
            AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {

            // Spring will ALWAYS call this filter for EVERY request. To avoid it, we put a
            // arbitrary condition to bypass the authorization and continue on the chain. In
            // the end spring will give it an "annonymous" user and will use that whenever
            // it need to secure a resource. Example would be static resource that we
            // "permitAll()". These request will also tigger this filter and hopefully the
            // condition doesn't pass and we continue down the filter chain to get the
            // "annonymous" user which can access static resourse because of "permitAll"
            String key = request.getParameter("key");
            if (key != null) {
                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("name",
                        "password");

                String username = authRequest.getName();
                this.logger.trace(LogMessage.format("Found key '%s' header", username));
                if (authenticationIsRequired(username)) {
                    Authentication authResult = getAuthenticationManager().authenticate(authRequest);
                    SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
                    context.setAuthentication(authResult);
                    this.securityContextHolderStrategy.setContext(context);
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug(LogMessage.format("Set SecurityContextHolder to %s", authResult));
                    }
                    this.rememberMeServices.loginSuccess(request, response, authResult);
                    this.securityContextRepository.saveContext(context, request, response);
                    onSuccessfulAuthentication(request, response, authResult);
                }
            }
        } catch (AuthenticationException ex) {
            this.securityContextHolderStrategy.clearContext();
            this.logger.debug("Failed to process authentication request", ex);
            this.rememberMeServices.loginFail(request, response);
            onUnsuccessfulAuthentication(request, response, ex);
            if (this.ignoreFailure) {
                chain.doFilter(request, response);
            } else {
                this.authenticationEntryPoint.commence(request, response, ex);
            }
            return;
        }

        chain.doFilter(request, response);
    }

}
