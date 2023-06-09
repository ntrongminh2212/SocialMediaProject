package com.example.cloudgateway.filter;

import com.example.cloudgateway.dto.UserDTO;
import com.example.cloudgateway.feignclient.UserClient;
import com.example.cloudgateway.service.RequestBodyRewriteFunction;
import com.google.common.collect.ImmutableMap;
import org.apache.hc.core5.http.ContentType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.naming.PartialResultException;
import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AuthenticationFilter implements GlobalFilter {

    Logger logger = Logger.getLogger(AuthenticationFilter.class);
    private static final String[] PASS_FILTER_PATHS = {
            "/user/login",
            "/user/register",
            "/ws/info",
            "/ws",
            "/conversation/**",
            "/post-service/v3/api-docs",
            "/friend-service/v3/api-docs",
            "/message-service/v3/api-docs"
    };
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CONTENT_TYPE_FORM_DATA ="multipart/form-data";
    private static final String CONTENT_TYPE = "Content-Type";
    private UserClient userClient;
    private final ModifyRequestBodyGatewayFilterFactory factory;

    @Autowired
    private WebClient webClient;

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
        ServerHttpRequest request = exchange.getRequest();

        String contentType = request.getHeaders().getFirst(CONTENT_TYPE);
        logger.info(contentType);
        String method = request.getMethod().name();
        logger.info("[Method] " + method);

        String path = exchange.getRequest().getPath().toString();
        if (Arrays.stream(PASS_FILTER_PATHS)
                .anyMatch(x -> x.compareToIgnoreCase(path) == 0)) {

        } else {
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return Mono.empty();
            }

            String authToken = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if (authToken != null && authToken.startsWith("Bearer ")) {
                authToken = authToken.substring(7);
                UserDTO userDTO = userClient.authenticate(authToken).getBody();

//                if (userDTO ==null){
//                    exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
//                    return Mono.empty();
//                }
                if (contentType != null && (HttpMethod.POST.name().equalsIgnoreCase(method) ||
                        HttpMethod.PUT.name().equalsIgnoreCase(method) ||
                        HttpMethod.DELETE.name().equalsIgnoreCase(method))
                        && (contentType.contains(CONTENT_TYPE_JSON)
                        || contentType.contains(CONTENT_TYPE_FORM_DATA))
                        && userDTO!=null
                ) {
                    exchange.mutate().request(
                            exchange.getRequest().mutate()
                                    .header("userId", String.valueOf(userDTO.getUserId()))
                                    .header("role", userDTO.getRole().name())
                                    .build());

                    ModifyRequestBodyGatewayFilterFactory.Config modifyRequestConfig = new ModifyRequestBodyGatewayFilterFactory.Config();
                    if (contentType.contains(CONTENT_TYPE_JSON)) {
                        modifyRequestConfig.setContentType(ContentType.APPLICATION_JSON.getMimeType());
                    }else if (contentType.contains(CONTENT_TYPE_FORM_DATA)) {
                        modifyRequestConfig.setContentType(ContentType.MULTIPART_FORM_DATA.getMimeType());
                    }

                    modifyRequestConfig.setRewriteFunction(Map.class, Map.class, (exchange1, originalRequestBody) -> {
//                                logger.info(originalRequestBody);
                                if (userDTO!=null) {
                                    originalRequestBody.put("userId", userDTO.getUserId());
                                }
                                return Mono.just(originalRequestBody);
                            });

                    return new ModifyRequestBodyGatewayFilterFactory().apply(modifyRequestConfig).filter(exchange, chain);
                }

                if (HttpMethod.GET.name().equalsIgnoreCase(method) && userDTO!=null) {
                    return chain.filter(exchange.mutate().request(
                                    exchange.getRequest().mutate()
                                            .header("userId", String.valueOf(userDTO.getUserId()))
                                            .header("role", userDTO.getRole().name())
                                            .build())
                            .build());

                }
            }
        }
        logger.info(exchange.getRequest().getHeaders());
        return chain.filter(exchange);
    }

}











