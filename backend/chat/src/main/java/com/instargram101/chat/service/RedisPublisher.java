package com.instargram101.chat.service;

import com.instargram101.chat.dto.response.MessageResponse;
import com.instargram101.chat.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisPublisher {

    private final RedisTemplate redisTemplate;

    public void publishMessage(Long roomId, MessageResponse message) {
        log.info("publish try in redis");

        redisTemplate.convertAndSend("room/"+roomId, message);
        log.info("published: "+message.toString());

    }
}
