package com.epam.esm.config;

import com.epam.esm.exception.AuthenticationExceptionHandler;
import com.epam.esm.exception.ErrorResponseBuilder;
import com.epam.esm.filter.AuthenticationFilter;
import com.epam.esm.filter.AuthorizationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.epam.esm.dto.UserRoles.ADMIN_ROLE;
import static com.epam.esm.dto.UserRoles.USER_ROLE;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    private final AuthorizationFilter authorizationFilter;
    private final AuthenticationExceptionHandler exceptionHandler;

    private final ObjectMapper objectMapper;
    private final ErrorResponseBuilder errorBuilder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManagerBean(), objectMapper, errorBuilder);
        authenticationFilter.setFilterProcessesUrl("/token/authenticate");
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.csrf().disable();
        http.authorizeRequests().antMatchers(GET, "/certificates/**").permitAll();
        http.authorizeRequests().antMatchers(POST, "/token/**", "/token/*").permitAll();
        http.authorizeRequests().antMatchers(GET, "/tags/**", "/orders/**", "/users/*").hasAuthority(USER_ROLE);
        http.authorizeRequests().antMatchers(POST, "/orders/**").hasAuthority(USER_ROLE);
        /*http.authorizeRequests().anyRequest().hasAuthority("ADMIN");*/
        http.authorizeRequests().antMatchers(GET, "/users/*/orders/**").hasAuthority(ADMIN_ROLE);
        http.authorizeRequests().antMatchers(POST, "/certificates/**", "/tags/**" , "/users/**").hasAuthority(ADMIN_ROLE);
        http.authorizeRequests().antMatchers(PATCH, "/certificates/**", "/tags/**", "/orders/**", "/users/**").hasAuthority(ADMIN_ROLE);
        http.authorizeRequests().antMatchers(DELETE, "/certificates/**", "/tags/**", "/orders/**", "/users/**").hasAuthority(ADMIN_ROLE);
       /* http.authorizeRequests().anyRequest().authenticated();*/
        http.addFilter(authenticationFilter);
        http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling().authenticationEntryPoint(exceptionHandler)
                .accessDeniedHandler(exceptionHandler);

    }
}