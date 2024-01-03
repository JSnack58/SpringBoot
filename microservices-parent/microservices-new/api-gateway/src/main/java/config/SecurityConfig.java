package config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;



@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    String issuerUri;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity){
        serverHttpSecurity
                // Disable csrf protection for development.
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                // Configure the authorization to permit all exchanges on the /eureka/**
                // to authenticated users.
                .authorizeExchange(exchange ->
                        exchange
                        .pathMatchers("/eureka/**")
                        .permitAll()
                        .anyExchange()
                        .authenticated()
                )
                // Provides access to the oAuth2 KeyCloak server for authorization.
                .oauth2ResourceServer((oAuth2ResourceServerSpec) ->
                        oAuth2ResourceServerSpec
                                .jwt( (jwtSpec)->
                                        jwtSpec.jwtDecoder(JwtDecoders.fromIssuerLocation(issuerUri)))) ;
      return serverHttpSecurity.build();
    }
}
