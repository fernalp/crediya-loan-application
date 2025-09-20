package com.crediya.solicitudes.security.config.jwt;

import com.crediya.solicitudes.security.exceptions.CustomAuthenticationException;
import com.crediya.solicitudes.security.services.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationConverter implements ServerAuthenticationConverter {

    private static final String MESSAGE_INVALID_TOKEN = "El token es inválido, por favor inicia sesión de nuevo!!";
    private final JwtProvider jwtProvider;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.empty();
        }
        String token = authHeader.substring(7);
        return jwtProvider.extractClaims(token)
                .onErrorResume(e -> Mono.error(new CustomAuthenticationException(MESSAGE_INVALID_TOKEN)))
                .map(claims -> {
                    String username = claims.getSubject();
                    String role = claims.get("role", String.class);
                    var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
                    return new UsernamePasswordAuthenticationToken(username, null, authorities);
                })
                ;
    }
}
