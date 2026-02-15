package com.example.hms.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static com.example.hms.entity.type.RoleType.ADMIN;
import static com.example.hms.entity.type.RoleType.DOCTOR;

@RequiredArgsConstructor
@Configuration
public class RoutesConfig {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf(csrfconfig->csrfconfig.disable())
                .sessionManagement(sessionconfig->sessionconfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/public/**","/auth/**").permitAll()
                        .requestMatchers("/admin/**").hasRole(ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE,"/admin/**").hasAllAuthorities("appointment:delete")
                        .requestMatchers("/doctors/**").hasAnyRole(DOCTOR.name(), ADMIN.name())
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oAuth2 -> oAuth2
                        .failureHandler((request,response,exception)->{
                            handlerExceptionResolver.resolveException(request,response,null,exception);
                        })
                        .successHandler(oAuth2SuccessHandler))
                .exceptionHandling(exceptionHandlingConfigurer ->
                        exceptionHandlingConfigurer.accessDeniedHandler((request, response, accessDeniedException) -> {
                            handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
                        }));
        return httpSecurity.build();


    }
}
