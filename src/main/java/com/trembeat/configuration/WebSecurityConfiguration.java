package com.trembeat.configuration;

import com.trembeat.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


/**
 * Spring Boot Security configuration class
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
    @Autowired
    private UserService _userService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> {
                    request.requestMatchers(
                                    "/",
                                    "/about*",
                                    "/login*",
                                    "/register*",
                                    "/sound",
                                    "/sound/*",
                                    "/api/**",
                                    "/js/**",
                                    "/user/*")
                            .permitAll()
                            .anyRequest()
                            .authenticated();
                })
                .formLogin(form -> {
                    form.loginPage("/login")
                            .permitAll();
                })
                .logout(logout -> {
                    logout.permitAll();
                })
                .userDetailsService(_userService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
