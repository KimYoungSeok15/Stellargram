package com.instargram101.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{

    // 엔드 포인트 이후 메세지 브로커 설정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/subscribe");
        config.setApplicationDestinationPrefixes("/publish");
    }

    // 웹소켓 엔드포인트 지정. 추후 인터셉터 추가시 여기에.
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws").setAllowedOrigins("*"); //.withSockJS(); 필요하면 추가할 것
    }

}
