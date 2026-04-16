package org.example.backend.Config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
//        http
//                .csrf(csrf -> csrf.ignoringRequestMatchers(
//                        "/api/register",
//                        "/api/users/**"
//                ))
//
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(HttpMethod.POST, "/api/register").permitAll()
//                        .requestMatchers(HttpMethod.PUT, "/api/users/**").permitAll()
//                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//
//                .httpBasic(Customizer.withDefaults());
//        return http.build();
//    }


    //some test users
    @Bean
    @Profile("dev")
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder) {
        UserDetails testUser = org.springframework.security.core.userdetails.User.builder()
                .username("testuser")
                .passwordEncoder(passwordEncoder::encode)
                .password("testpassword")
                .roles("CUSTOMER").build();

        UserDetails testStaff = org.springframework.security.core.userdetails.User.builder()
                .username("teststaff")
                .passwordEncoder(passwordEncoder::encode)
                .password("testpassword")
                .roles("STAFF").build();

        UserDetails testAdmin = org.springframework.security.core.userdetails.User.builder()
                .username("testadmin")
                .passwordEncoder(passwordEncoder::encode)
                .password("testpassword")
                .roles("ADMIN").build();
        return new InMemoryUserDetailsManager(testUser, testStaff, testAdmin);

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        // enable CORS support handled by the CorsConfigurationSource bean
        http.cors(withDefaults());
        http.authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/**").permitAll()
//			.requestMatchers(HttpMethod.GET,"/api/users").authenticated()
//			.requestMatchers(HttpMethod.GET,"/api/users/**").hasRole("CUSTOMER")
//			.requestMatchers(HttpMethod.POST,"/api/register").hasRole("ADMIN")
//			.requestMatchers(HttpMethod.PUT,"/api/users/**").hasRole("ADMIN")
//			.requestMatchers(HttpMethod.DELETE,"/api/users/**").hasRole("ADMIN")
        );

        http.httpBasic(withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // allow the frontend origin in development; adjust as needed for production
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role("ADMIN").implies("STAFF")
                .role("STAFF").implies("CUSTOMER").build();
    }


    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT name, password, 1 FROM `user` WHERE name = ?");

        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT u.name, CONCAT('ROLE_', r.name) FROM `user` u JOIN `role` r ON u.role_id = r.role_id WHERE u.name = ?");

        return jdbcUserDetailsManager;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsManager userDetailsManager,
            org.springframework.security.config.annotation.web.builders.HttpSecurity http,
            PasswordEncoder passwordEncoder,
            @Autowired(required = false) InMemoryUserDetailsManager inMemoryUserDetailsManager) {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);

        if (inMemoryUserDetailsManager != null) {
            builder.userDetailsService(inMemoryUserDetailsManager).passwordEncoder(passwordEncoder);
        }
        builder.userDetailsService(userDetailsManager).passwordEncoder(passwordEncoder);

        return builder.build();
    }

}
