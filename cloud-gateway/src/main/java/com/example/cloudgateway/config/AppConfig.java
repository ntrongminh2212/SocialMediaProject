package com.example.cloudgateway.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;

@Configuration
public class AppConfig {

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder){
//        /post-service/v3/api-docs
//        /friend-service/v3/api-docs

//        lb://USER-SERVICE
//        lb://POST-SERVICE
//        lb://MESSAGE-SERVICE
        return routeLocatorBuilder
                .routes()
                .route(r->r.path("/post-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://POST-SERVICE"))
                .route(r->r.path("/friend-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://USER-SERVICE"))
                .route(r->r.path("/message-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://MESSAGE-SERVICE"))
                .build();
    }
}
