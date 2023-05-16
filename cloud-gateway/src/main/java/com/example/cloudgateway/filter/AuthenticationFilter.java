package com.example.cloudgateway.filter;

import org.apache.log4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GlobalFilter {

    Logger logger = Logger.getLogger(AuthenticationFilter.class);

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain
    ) {

        if (exchange.getRequest().getPath().toString().compareTo("/user/login")==0){

        }else {
            String authToken = exchange.getRequest().getHeaders().getFirst("Authorization");
            logger.info("[Request come]" +authToken);

        }
        return chain.filter(exchange);
    }
}











