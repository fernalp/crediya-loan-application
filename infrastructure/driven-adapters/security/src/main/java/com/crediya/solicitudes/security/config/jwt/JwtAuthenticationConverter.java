package com.crediya.solicitudes.security.config.jwt;

import com.crediya.solicitudes.model.constants.LoanConstants;
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

    private final JwtProvider jwtProvider;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(LoanConstants.BEARER_PREFIX)) {
            return Mono.empty();
        }
        String token = authHeader.substring(LoanConstants.BEARER_PREFIX_LENGTH);
        return jwtProvider.extractClaims(token)
                .onErrorResume(e -> Mono.error(new CustomAuthenticationException(LoanConstants.ERROR_MESSAGE_INVALID_TOKEN)))
                .map(claims -> {
                    String username = claims.getSubject();
                    String role = claims.get(LoanConstants.ROLE_CLAIM, String.class);
                    var authorities = List.of(new SimpleGrantedAuthority(LoanConstants.AUTHORITY_PREFIX + role));
                    return new UsernamePasswordAuthenticationToken(username, null, authorities);
                })
                ;
    }
}
