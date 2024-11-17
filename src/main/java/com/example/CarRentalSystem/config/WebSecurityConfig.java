package com.example.CarRentalSystem.config;

import com.example.CarRentalSystem.infrastructure.JwtAuthFilter;
import com.example.CarRentalSystem.service.auth.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;



@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
//    private final JwtService jwtService;

//    public WebSecurityConfig() {
//        this.jwtService = jwtService;
//    }


    @Bean
    public JwtAuthFilter jwtAuthFilter(JwtService jwtService) {
        return new JwtAuthFilter(jwtService);
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf((csrf) -> csrf
//                        .csrfTokenRepository(new HttpSessionCsrfTokenRepository()).disable()
//                ) // Отключение CSRF для упрощения (в продакшене нужно включить)
//                .authorizeHttpRequests(authz -> authz
//                        .requestMatchers(USER_LIST).hasRole(USER)
//
//
////                        .anyRequest().permitAll() // Остальные запросы разрешены для всех
//                );
////                .httpBasic(); // Использование Basic Authentication
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http
                .csrf((csrf) -> csrf
                        .csrfTokenRepository(new HttpSessionCsrfTokenRepository()).disable()
                )
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/booking/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Добавляем фильтр JWT

        return http.build();
    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .logout(logout -> logout
//                        .deleteCookies("JSESSIONID")
//                        .logoutUrl("/logout"))
//                .authorizeHttpRequests(auth ->
//                        auth
//                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
//                                .requestMatchers(HttpMethod.POST, "/users/registration").anonymous()
//                                .requestMatchers(ADMIN_LIST).hasRole("ADMIN")
//                                .requestMatchers(USER_LIST).hasRole("USER")
//                                .requestMatchers("/class/**").hasAnyRole("STUDENT", "TEACHER", "ADMIN")
//                                .anyRequest().authenticated()
//                )
//                .headers(headers -> headers.cacheControl(Customizer.withDefaults()).disable())
//                .httpBasic(Customizer.withDefaults())
//                .formLogin(Customizer.withDefaults())
//                .exceptionHandling(exceptionHandling -> exceptionHandling
//                        .accessDeniedHandler(myAccessDeniedHandler));
//
//        return http.build();
//    }
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

//    @Bean
//    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder =
//                http.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder.inMemoryAuthentication()
//                .withUser("user").password(passwordEncoder().encode("password")).roles("USER1");
//        return authenticationManagerBuilder.build();
//    }
}