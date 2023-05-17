package com.example.cloudgateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

public class RequestBodyRewriteFunction implements RewriteFunction<String, String> {

    private final Map<String, Integer> values;

    public RequestBodyRewriteFunction(Map<String, Integer> values) {
        this.values = values;
    }

    @Override
    public Publisher<String> apply(ServerWebExchange serverWebExchange, String oldBody) {
        /* do things here */
        /* example: */
        try {
            String newBody = new ObjectMapper().writeValueAsString(values);
            return Mono.just(newBody);
        } catch (Exception e) {
            /* error parsing values to json, do something else */
            return Mono.just(oldBody);
        }
    }
}