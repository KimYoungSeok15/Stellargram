package com.instargram101.chat.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 웹소켓 연결 엔드포인트 지정. 추후 인터셉터 추가시 여기에.
    // 모든 오리진 허용 상태
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*"); //.withSockJS(); 필요하면 추가할 것
    }

    // 엔드 포인트 이후 메세지 브로커 설정
    // 메세지 송수신 처리 시작 지점
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // subscribe로 보내면 이곳을 거쳐서 프론트로 데이터 전달
        registry.enableSimpleBroker("/subscribe");

        // publish로 데이터 받으면 이곳을 거쳐서 URI만 @MessageMapping에 매핑됨
        // publish/chat/message면 /chat/message 만 @MessageMapping에 매핑됨
        // 해당 주소로 시작하는
        registry.setApplicationDestinationPrefixes("/publish");
    }

    // 사용자가 웹 소켓 연결에 연결 될 때와 끊길 때 추가기능 등을 위한 설정 및 인터셉터 추가 위치
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(stompHandler);
    }

}
