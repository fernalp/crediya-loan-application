package com.crediya.solicitudes.security.config.security;

import com.crediya.solicitudes.security.config.jwt.JwtAuthenticationConverter;
import com.crediya.solicitudes.security.exceptions.CustomAccessDeniedException;
import com.crediya.solicitudes.security.exceptions.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import reactor.core.publisher.Mono;

@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/api/doc/swagger-ui.html",
            "/api/doc/api-docs/**",
            "/api/doc/swagger-ui/**",
            "/docs",
            "/scalar/**"
    };

    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final CustomAccessDeniedException customAccessDeniedException;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        var authFilter = new AuthenticationWebFilter(
                (ReactiveAuthenticationManager) Mono::just
        );
        authFilter.setServerAuthenticationConverter(jwtAuthenticationConverter);

        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .exceptionHandling(
                        exceptionHandlingSpec -> exceptionHandlingSpec
                                .accessDeniedHandler(customAccessDeniedException)
                                .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(AUTH_WHITELIST).permitAll()
                        .anyExchange().authenticated()

                ).addFilterAt(authFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }
}
