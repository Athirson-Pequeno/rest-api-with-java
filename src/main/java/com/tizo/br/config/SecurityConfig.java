package com.tizo.br.config;

import com.tizo.br.security.jwt.JwtTokenFilter;
import com.tizo.br.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Bean
    AuthenticationManager authenticationManagerBean
            (AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();

        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder("tizo", 8, 185000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
        encoders.put("pbkdf2", pbkdf2PasswordEncoder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2PasswordEncoder);
        return passwordEncoder;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        JwtTokenFilter customFilter = new JwtTokenFilter(tokenProvider);

        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(
                        authorizeHttpRequests -> authorizeHttpRequests
                                .requestMatchers(
                                        "/auth/**",
                                        "/api/users/v1",
                                        "/api/users/v1/recordUser/commonUser",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/api/product/v1/findAll")
                                .permitAll()
                                .requestMatchers(
                                        "/api/users/v1/recordUser/managerUser",
                                        "/api/users/v1/findAll",
                                        "/api/sectors/v1",
                                        "/api/product/v1/createProduct")
                                .hasAnyAuthority("MANAGER", "ADMIN")
                                .requestMatchers("/api/users/v1/**").hasAnyAuthority("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/products/v1/**").hasAnyAuthority("ADMIN","MANAGER")
                                .requestMatchers(HttpMethod.PUT, "/api/products/v1/**").hasAnyAuthority("ADMIN","MANAGER")
                                .requestMatchers(HttpMethod.POST, "/api/products/v1/**").hasAnyAuthority("ADMIN","MANAGER")
                                .anyRequest().permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}