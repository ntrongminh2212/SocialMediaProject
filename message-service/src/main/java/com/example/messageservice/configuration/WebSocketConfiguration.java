package com.example.messageservice.configuration;

import com.example.messageservice.dto.MessageDTO;
import com.example.messageservice.dto.UserDTO;
import com.example.messageservice.feignclient.UserClient;
import com.example.messageservice.service.MessageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    Logger logger = Logger.getLogger(WebSocketConfiguration.class);
    @Autowired
    private UserClient userClient;
    @Autowired
    private MessageService messageService;
    private SimpleMessageConverter simpleMessageConverter = new SimpleMessageConverter();

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/conversation");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                LinkedMultiValueMap<String, Object> nativeHeaders = (LinkedMultiValueMap<String, Object>) message.getHeaders().get("nativeHeaders");
                if (nativeHeaders.containsKey("Authorization")) {
                    String authToken = nativeHeaders.get("Authorization").get(0).toString();
                    if (authToken != null && authToken.startsWith("Bearer ")) {
                        authToken = authToken.substring(7);
                        UserDTO userDTO = userClient.authenticate(authToken).getBody();
                        if (userDTO != null) {
                            if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
                                if (userDTO != null) {
                                    String destination = nativeHeaders.get("destination").get(0).toString();
                                    Long conversationId = Long.parseLong(destination.substring(
                                            destination.lastIndexOf("/")+1
                                    ));

                                    if(!messageService.isParticipant(userDTO.getUserId(),conversationId)){
                                        throw new MessagingException("Not a participant!");
                                    }
                                }
                            }
                            if (StompCommand.SEND.equals(accessor.getCommand())) {
                                nativeHeaders.add("userId", userDTO.getUserId());
                            }
                        }
                    }
                }
                return message;
            }
        });
    }
}
