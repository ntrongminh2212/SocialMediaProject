package com.example.cloudgateway.filter;

import com.example.cloudgateway.dto.UserDTO;
import com.example.cloudgateway.feignclient.UserClient;
import com.example.cloudgateway.service.RequestBodyRewriteFunction;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthenticationFilter implements GlobalFilter {

    Logger logger = Logger.getLogger(AuthenticationFilter.class);
    private static final String[] PASS_FILTER_PATHS = {
            "/user/login"
    };
    private UserClient userClient;
    private final ModifyRequestBodyGatewayFilterFactory factory;

    @Autowired
    public AuthenticationFilter(@Lazy UserClient userClient, ModifyRequestBodyGatewayFilterFactory factory) {
        this.userClient = userClient;
        this.factory = factory;
    }

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain
    ) {
//        Map<String, String> requestBody = new HashMap<>();
//         exchange.getRequest().getBody()

//        ModifyRequestBodyGatewayFilterFactory.Config cfg = new ModifyRequestBodyGatewayFilterFactory.Config();
//        cfg.setRewriteFunction(String.class, String.class, new RequestBodyRewriteFunction(requestBody));
//
//        GatewayFilter modifyBodyFilter = factory.apply(cfg);

        String path = exchange.getRequest().getPath().toString();
        if (Arrays.stream(PASS_FILTER_PATHS)
                .anyMatch(x -> x.compareToIgnoreCase(path) == 0)) {

        } else {
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new RuntimeException("Missing authorization header");
            }

            String authToken = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if (authToken != null && authToken.startsWith("Bearer ")) {
                logger.info("[Request come]" + authToken);
                authToken = authToken.substring(7);
                UserDTO userDTO = userClient.authenticate(authToken).getBody();
                logger.info("[Response] ");

            }
        }
//        return modifyBodyFilter.filter(exchange, ch -> Mono.empty())
//                .then(chain.filter(exchange));
        return chain.filter(exchange);
    }
}











