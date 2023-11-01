package com.instargram101.chat.api;

import com.instargram101.chat.entity.ChatMessage;
import com.instargram101.chat.service.MessageService;
import com.instargram101.chat.service.RedisPublisher;
import com.instargram101.chat.service.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class WebSocketController {
    public final MessageService messageService;

    public final RedisMessageListenerContainer ListenerContainer;
    public final RedisPublisher redisPublisher;
    public final RedisSubscriber redisSubscriber;
    public final SimpMessagingTemplate messageTemplate;
    public final SimpMessageSendingOperations sendingOperations;

    // 특정 채팅방에 메세지 발행
    @MessageMapping("/room/{chatRoomId}")
    public void publishMessage(@DestinationVariable("chatRoomId") Long roomId, ChatMessage message) {
        message.setRoomId(roomId);
        messageService.sendMessageToRoom(roomId,message);
    }

    // 테스트
    @MessageMapping("/test")
    public void forTest() {
        log.info("WebSocket Test");
        System.out.println("WebSocket Test println");
    }
}
