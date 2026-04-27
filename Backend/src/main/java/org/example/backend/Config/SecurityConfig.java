package org.example.backend.Config;

import javax.sql.DataSource;

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

import java.util.Arrays;

@Configuration
public class SecurityConfig {

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

        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "http://localhost:5174",
                "http://localhost:3000"
        ));

        config.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        config.setAllowedHeaders(Arrays.asList(
                "Authorization", "Content-Type", "Accept"
        ));

        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
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