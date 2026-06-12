package com.example.api_gateway.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.security")
@AllArgsConstructor
@NoArgsConstructor
public class SecurityProperties {
    private List<Permission> permissions;

    @Data
    public static class Permission {
        private String path;
        private Boolean permitAll;
        private List<String> roles;
    }

}
