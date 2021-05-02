package com.naveen.microservice.wordwrap.controller.security;

import com.naveen.microservice.wordwrap.repository.ApiKeyRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class ApiKeyBasedAuthenticationSecurityConfig extends WebSecurityConfigurerAdapter {
    private final ApiKeyRepository apiKeyRepository;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        ApiKeyFetchFilter filter = new ApiKeyFetchFilter();

        filter.setAuthenticationManager(authentication -> {
            String principal = (String) authentication.getPrincipal();
            authentication.setAuthenticated(apiKeyRepository.isValidApiKey(principal));
            return authentication;
        });

        httpSecurity.
                antMatcher("/api/**")
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(filter)
                .addFilterBefore(new ExceptionTranslationFilter(new Http403ForbiddenEntryPoint()),
                        filter.getClass()
                )
                .authorizeRequests()
                .anyRequest()
                .authenticated();
    }
}
