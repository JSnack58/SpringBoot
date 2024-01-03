package com.example.discoveryserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class SecurityConfig {
    @Value("${eureka.username}")
    private String username;
    @Value("${eureka.password}")
    private String password;
    // Originally used the AuthenticationManagerBuilder but that implementation used
    // deprecated code(WebSecurityConfigurerAdapter, and withDefaultPasswordEncoder).
    // Springboot now uses InMemoryUserDetailsManager for the In-Memory Authorization.
    @Bean
    public InMemoryUserDetailsManager userDetailsService(){
        UserDetails user = User
                .withUsername(username)
                .password(password)
                .authorities("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    //    The AuthenticationProvider is the interface to authenticate the given credentials.
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // The passwordEncoder is required for the AuthenticationProviders. There will be no encrypted password
    // for development but there must be for production, a BCryptPasswordEncoder.
    @Bean
    public PasswordEncoder passwordEncoder(){
            return NoOpPasswordEncoder.getInstance();
    }

    // The AuthenticationManager manager which AuthenticationProvider to use for authentication.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    // Filter to authenticate users before providing access to the microservices.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authz)->authz.anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .authenticationProvider(authenticationProvider())
                .authenticationManager(authenticationManager(new AuthenticationConfiguration())
                );
        return httpSecurity.build();
    }
}
