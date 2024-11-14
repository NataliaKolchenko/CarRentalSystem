package com.example.CarRentalSystem.config;

import com.example.CarRentalSystem.enums.AppRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import static com.example.CarRentalSystem.config.utils.AuthorizationRightsRoles.USER;
import static com.example.CarRentalSystem.config.utils.AuthorizationRightsRoles.USER_LIST;


@Configuration
@EnableWebSecurity
public class WebConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf
                        .csrfTokenRepository(new HttpSessionCsrfTokenRepository()).disable()
                ) // Отключение CSRF для упрощения (в продакшене нужно включить)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(USER_LIST).hasRole(USER)


                        .anyRequest().permitAll() // Остальные запросы разрешены для всех
                );
//                .httpBasic(); // Использование Basic Authentication

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
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.inMemoryAuthentication()
                .withUser("user").password(passwordEncoder().encode("password")).roles("USER");
        return authenticationManagerBuilder.build();
    }
}