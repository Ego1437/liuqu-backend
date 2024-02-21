package com.omate.liuqu.config;

import org.springframework.beans.factory.annotation.Value;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${app.jwt.secretKey}")
    private String secretKey;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(secretKey);
    }

    private static final String[] AUTH_WHITELIST = {
            "/api/verification/**",
            "/api/activity/**",
            "/api/tags/**",
            "/api/partners/**",
            "/api/login",
            "/api/register",
            "/api/change-password",
            "/api/upload",

            // 测试用
            // "/api/orders/**",
            // "/api/events/createEvent",
            // "/api/tickets/createTicket",
            // "/api/check-favorite",
            // "/api/check-followed",
    };

    private static final String[] AUTH_USER = {
            "/api/notifications/**",
            "/api/updateUserInfoById",
            "/api/userInfo",
            "/api/refreshToken",
            "/api/orders/**",
            "/api/favorites/**",
            "/api/{userId}/favorite-activities",
            "/api/{userId}/followed-partners",
            "/api/delete/{userId}",
            "/api/check-favorite",
            "/api/check-followed",
    };

    private static final String[] AUTH_PARTNER = {
            "/api/orders/{orderId}/updateOrderStatusById",
            "/api/activity/createActivity",
            "/api/activity/updateActivity/**",
            "/api/events/createEvent",
            "/api/tickets/createTicket",
    };

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.addAllowedOriginPattern("*"); // allow all origins
                    // config.setAllowedOrigins(Arrays.asList("*")); // allow all origins
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // allow all
                                                                                                        // methods
                    config.setAllowedHeaders(Arrays.asList("*")); // allow all heads
                    config.setAllowCredentials(true); // allow all details
                    return config;
                })
                .and()
                .authorizeHttpRequests()
                .requestMatchers(AUTH_WHITELIST).permitAll() // 允许所有用户访问
                .requestMatchers(AUTH_USER).hasRole("USER") // 需要USER角色
                .requestMatchers(AUTH_PARTNER).hasRole("PARTNER") // 需要PARTNER角色
                .anyRequest().authenticated() // 其他所有请求需要身份验证
                // .requestMatchers("/api/**", "/image/**").permitAll()
                // .requestMatchers("/hello").authenticated()
                // .anyRequest().permitAll()
                .and()
                .addFilterBefore(
                        jwtAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .csrf().disable(); // 关闭CSRF保护
        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return new CorsFilter(source);
    }
}
