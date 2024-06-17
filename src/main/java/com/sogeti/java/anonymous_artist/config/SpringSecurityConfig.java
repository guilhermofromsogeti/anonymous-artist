package com.sogeti.java.anonymous_artist.config;

import com.sogeti.java.anonymous_artist.exception.handler.CustomAccessDeniedHandler;
import com.sogeti.java.anonymous_artist.filter.JwtRequestFilter;
import com.sogeti.java.anonymous_artist.service.CustomUserDetailsService;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    public SpringSecurityConfig(CustomUserDetailsService userDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder)
            throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return
                http
                        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .csrf(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests(auth -> auth
                                .requestMatchers(HttpMethod.POST, "/anonymous-artist/api/products/").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/anonymous-artist/api/products/").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/anonymous-artist/api/products/").permitAll()
                                .requestMatchers(HttpMethod.GET, "/anonymous-artist/api/products/{id}").permitAll()

                                .requestMatchers(HttpMethod.POST, "/anonymous-artist/api/cart/add/").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/anonymous-artist/api/cart/edit/").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/anonymous-artist/api/cart/").hasAnyRole("USER", "ADMIN")

                                .requestMatchers(HttpMethod.POST, "/anonymous-artist/api/order/").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/anonymous-artist/api/order/history/").hasAnyRole("USER", "ADMIN")

                                .requestMatchers(HttpMethod.POST, "/anonymous-artist/api/authenticate").permitAll()
                                .requestMatchers(HttpMethod.POST, "/anonymous-artist/api/login").permitAll()

                                .requestMatchers(HttpMethod.POST, "/anonymous-artist/api/image/*").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.GET, "/anonymous-artist/api/image/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/anonymous-artist/api/image/**").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.GET, "/anonymous-artist/api/product").permitAll()
                                .requestMatchers(HttpMethod.GET, "/anonymous-artist/api/product/{id}").permitAll()
                                .requestMatchers(HttpMethod.POST, "/anonymous-artist/api/product/").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/anonymous-artist/api/product/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/anonymous-artist/api/product/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/anonymous-artist/api/product/{id}/image").hasRole("ADMIN")

                                .anyRequest().permitAll())
                        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                        .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedHandler(accessDeniedHandler()))
                        .build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}

