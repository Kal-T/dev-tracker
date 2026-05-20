package com.devtracker.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Value("${app.cors.allowed-origin}")
    private String allowedOrigin;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Enable standard CORS mappings matching our source configuration
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // 2. Disable CSRF for stateless REST endpoints utilizing bearer tokens
            .csrf(AbstractHttpConfigurer::disable)
            // 3. Set Session state to stateless
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 4. Allow H2 database frames to render correctly
            .headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
            // 5. Customise authorization permissions
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/health").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
            )
            // 6. Place custom JWT validator filter prior to standard authentication filters
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allow specified local frontend origin dynamically loaded from application.yml
        configuration.setAllowedOrigins(List.of(allowedOrigin));
        // Permit typical REST API operations
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        // Allow security headers along with payload metadata
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        // Required for cookies/sessions authentication integration
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public org.springframework.boot.web.servlet.FilterRegistrationBean<org.springframework.web.filter.ShallowEtagHeaderFilter> shallowEtagHeaderFilter() {
        org.springframework.boot.web.servlet.FilterRegistrationBean<org.springframework.web.filter.ShallowEtagHeaderFilter> filterRegistrationBean 
            = new org.springframework.boot.web.servlet.FilterRegistrationBean<>(new org.springframework.web.filter.ShallowEtagHeaderFilter());
        filterRegistrationBean.addUrlPatterns("/api/tasks/*");
        filterRegistrationBean.setName("etagFilter");
        return filterRegistrationBean;
    }
}
