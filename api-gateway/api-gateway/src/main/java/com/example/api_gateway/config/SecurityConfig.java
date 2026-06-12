package com.example.api_gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityProperties securityProperties;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        // Chỉnh sửa lại pathMatchers đọc từ file yaml
        http
                .csrf(csrf ->csrf.disable())
//                .authorizeExchange(exchanges -> exchanges
//                        .pathMatchers("/api/v1/order/**").hasAnyAuthority("ROLE_USER")
//                        .pathMatchers("/api/v1/products/**").hasAnyAuthority("ROLE_USER")
//                        .anyExchange().authenticated()
//                )

                .authorizeExchange(exchanges -> {

                    securityProperties.getPermissions().forEach(permission -> {

                        if (Boolean.TRUE.equals(permission.getPermitAll())) {

                            exchanges.pathMatchers(permission.getPath())
                                    .permitAll();

                        } else {

                            exchanges.pathMatchers(permission.getPath())
                                    .hasAnyAuthority(
                                            permission.getRoles().toArray(new String[0])
                                    );
                        }
                    });

                    exchanges.anyExchange().authenticated();
                })

                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );
        return http.build();
    }

    private Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
            if (resourceAccess == null) {
                return List.of();
            }

            Map<String, Object> backendAccess = (Map<String, Object>) resourceAccess.get("backend");
            if (backendAccess == null) {
                return List.of();
            }

            List<String> roles = (List<String>) backendAccess.get("roles");
            if(roles == null) return List.of();

            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    .collect(Collectors.toList());
        });

        // TODO
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }
}
