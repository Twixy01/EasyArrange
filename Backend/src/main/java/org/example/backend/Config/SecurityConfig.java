package org.example.backend.Config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

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
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails testUser = org.springframework.security.core.userdetails.User.builder()
                .username("testuser")
                .password("{noop}testpassword")
                .roles("customer").build();

        UserDetails testStaff = org.springframework.security.core.userdetails.User.builder()
                .username("teststaff")
                .password("{noop}testpassword")
                .roles("staff").build();

        UserDetails testAdmin = org.springframework.security.core.userdetails.User.builder()
                .username("testadmin")
                .password("{noop}testpassword")
                .roles("admin").build();
        return new InMemoryUserDetailsManager(testUser, testStaff, testAdmin);

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role("admin").implies("staff")
                .role("staff").implies("customer").build();
    }


    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT email, password, 1 FROM `user` WHERE email = ?");

        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT u.email, r.name FROM `user` u JOIN `role` r ON u.role_id = r.role_id WHERE u.email = ?");

        return jdbcUserDetailsManager;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            InMemoryUserDetailsManager inMemoryUserDetailsManager,
            UserDetailsManager userDetailsManager,
            org.springframework.security.config.annotation.web.builders.HttpSecurity http,
            PasswordEncoder passwordEncoder) {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);

        builder.userDetailsService(inMemoryUserDetailsManager).passwordEncoder(passwordEncoder);
        builder.userDetailsService(userDetailsManager).passwordEncoder(passwordEncoder);

        return builder.build();
    }



}
