package com.busanit501.pesttestproject0909.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 정적 리소스를 처리할 경로와 실제 리소스가 위치한 폴더를 명시적으로 설정
        registry.addResourceHandler("/**")  // 모든 경로에 대한 요청을 처리
                .addResourceLocations("classpath:/templates/", "classpath:/static/");
    }
}