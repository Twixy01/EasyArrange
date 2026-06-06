package org.example.backend.Config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class SecurityConfig {
    @Value("${frontend.url}")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors(withDefaults());

        http.csrf(AbstractHttpConfigurer::disable);

        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests(authorize -> authorize
                    .anyRequest().permitAll()
//                // CORS preflight
//                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

//                // Public user endpoints
//                .requestMatchers(HttpMethod.POST, "/api/users/auth/login").permitAll()
//                .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
//
//                // Current user
//                .requestMatchers(HttpMethod.PUT, "/api/users/me")
//                .hasAnyRole("CUSTOMER", "STAFF", "ADMIN")
//
//                // Admin user management
//                .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.GET, "/api/users/*").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.PUT, "/api/users/admin/**").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
//
//                // Services - public read, admin write
//                .requestMatchers(HttpMethod.GET, "/api/services/**").permitAll()
//                .requestMatchers(HttpMethod.POST, "/api/services/**").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.PUT, "/api/services/**").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.DELETE, "/api/services/**").hasRole("ADMIN")
//
//                // Staff - public read, admin write
//                .requestMatchers(HttpMethod.GET, "/api/staff/**").permitAll()
//                .requestMatchers(HttpMethod.POST, "/api/staff/register").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.DELETE, "/api/staff/**").hasRole("ADMIN")
//
//                // Shifts
//                .requestMatchers(HttpMethod.GET, "/api/shifts/**")
//                .hasAnyRole("STAFF", "ADMIN")
//                .requestMatchers(HttpMethod.POST, "/api/shifts/**").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.PUT, "/api/shifts/**").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.DELETE, "/api/shifts/**").hasRole("ADMIN")
//
//                // Staff-Shifts
//                .requestMatchers(HttpMethod.GET, "/api/staff-shifts/**")
//                .hasAnyRole("STAFF", "ADMIN")
//                .requestMatchers(HttpMethod.POST, "/api/staff-shifts/**").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.DELETE, "/api/staff-shifts/**").hasRole("ADMIN")
//
//                // Staff-Services - public read, admin write
//                .requestMatchers(HttpMethod.GET, "/api/staff-services/**").permitAll()
//                .requestMatchers(HttpMethod.POST, "/api/staff-services/**").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.DELETE, "/api/staff-services/**").hasRole("ADMIN")
//
//                // Booking public available slots
//                .requestMatchers(HttpMethod.GET, "/api/bookings/staff/*/available-slots")
//                .permitAll()
//
//                // Customer can create/cancel/view own booking, backend must check ownership
//                .requestMatchers(HttpMethod.POST, "/api/bookings/create")
//                .hasAnyRole("CUSTOMER", "STAFF", "ADMIN")
//                .requestMatchers(HttpMethod.POST, "/api/bookings/cancel/**")
//                .hasAnyRole("CUSTOMER", "STAFF", "ADMIN")
//                .requestMatchers(HttpMethod.GET, "/api/bookings/user/**")
//                .hasAnyRole("CUSTOMER", "STAFF", "ADMIN")
//                .requestMatchers(HttpMethod.GET, "/api/bookings/*")
//                .hasAnyRole("CUSTOMER", "STAFF", "ADMIN")
//
//                // Staff/admin booking management
//                .requestMatchers(HttpMethod.GET, "/api/bookings/**")
//                .hasAnyRole("STAFF", "ADMIN")
//                .requestMatchers(HttpMethod.PUT, "/api/bookings/**")
//                .hasAnyRole("STAFF", "ADMIN")
//                .requestMatchers(HttpMethod.DELETE, "/api/bookings/**")
//                .hasRole("ADMIN")
//
//                // Calendar blocks
//                .requestMatchers(HttpMethod.GET, "/api/calendar-blocks/**")
//                .hasAnyRole("STAFF", "ADMIN")
//                .requestMatchers(HttpMethod.POST, "/api/calendar-blocks/**")
//                .hasAnyRole("STAFF", "ADMIN")
//                .requestMatchers(HttpMethod.PUT, "/api/calendar-blocks/**")
//                .hasAnyRole("STAFF", "ADMIN")
//                .requestMatchers(HttpMethod.DELETE, "/api/calendar-blocks/**")
//                .hasAnyRole("STAFF", "ADMIN")
//
//                // Fallback
//                .anyRequest().authenticated()
        );

        http.httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(resolveAllowedOriginPatterns());
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setExposedHeaders(Arrays.asList("Authorization", "Location"));

        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }

    private List<String> resolveAllowedOriginPatterns() {
        if (frontendUrl == null || frontendUrl.isBlank()) {
            return Arrays.asList("http://localhost:5173", "http://127.0.0.1:5173");
        }

        List<String> patterns = new ArrayList<>();
        for (String candidate : frontendUrl.split(",")) {
            String pattern = normalizeOriginOrPattern(candidate);
            if (pattern != null) {
                patterns.add(pattern);
            }
        }

        if (patterns.isEmpty()) {
            return Arrays.asList("http://localhost:5173", "http://127.0.0.1:5173");
        }
        return patterns;
    }

    private String normalizeOriginOrPattern(String rawValue) {
        if (rawValue == null) {
            return null;
        }

        String value = rawValue.trim();
        if (value.isEmpty()) {
            return null;
        }

        // Keep explicit wildcard patterns (e.g. https://*.pages.dev) as-is.
        if (value.contains("*")) {
            return value;
        }

        try {
            URI uri = new URI(value);
            String scheme = uri.getScheme();
            String host = uri.getHost();

            // If parsing fails into a standard URI host, keep original for visibility.
            if (scheme == null || host == null) {
                return value;
            }

            int port = uri.getPort();
            return port == -1 ? scheme + "://" + host : scheme + "://" + host + ":" + port;
        } catch (URISyntaxException ex) {
            return value;
        }
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role("ADMIN").implies("STAFF")
                .role("STAFF").implies("CUSTOMER")
                .build();
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager =
                new JdbcUserDetailsManager(dataSource);

        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT email, password, 1 FROM `user` WHERE email = ?"
        );

        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                """
                        SELECT u.email, CONCAT('ROLE_', r.name)
                        FROM `user` u
                        JOIN `role` r ON u.role_id = r.role_id
                        WHERE u.email = ?
                        """
        );

        return jdbcUserDetailsManager;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            PasswordEncoder passwordEncoder,
            UserDetailsManager userDetailsManager
    ) throws Exception {

        AuthenticationManagerBuilder builder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        builder.userDetailsService(userDetailsManager)
                .passwordEncoder(passwordEncoder);

        return builder.build();
    }
}