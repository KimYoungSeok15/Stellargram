package com.instargram101.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.instargram101.chat.dto.response.MessageResponse;
import com.instargram101.chat.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;

    private final RedisTemplate redisTemplate;

    private final SimpMessageSendingOperations messageTemplate;

    // Redis에서 발행된 메세지를 대기하고있던 onMessage가 받아 처리한다
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try{
            // 레디스에서 발행된 데이터 deserialize
            String publishedMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());

            // ChatMessage 객체로 매핑
            MessageResponse newMessage = objectMapper.readValue(publishedMessage,MessageResponse.class);

            // 해당 채널을 구독중인 웹소켓 연결로 메세지 보내기
            messageTemplate.convertAndSend("/subscribe/room/"+newMessage.getRoomId(),newMessage);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
