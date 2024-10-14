package com.busanit501.pesttestproject0909.config;

import com.busanit501.pesttestproject0909.util.JWTUtil;
import groovy.util.logging.Log4j2;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SwaggerConfig {
    private final JWTUtil jwtUtil;
    private final APIUserDetailsService apiUserDetailsService;
}
