package com.instargram101.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TopicServiceImpl implements TopicService{
    private final RedisMessageListenerContainer listenerContainer;

    // 토픽 추가
    @Override
    public void addRoomTopicToRedis(MessageListener listener,Long roomId) {
        listenerContainer.addMessageListener(listener, new ChannelTopic("room/" + roomId));
    }

    // 토픽 삭제
    @Override
    public void removeRoomTopicToRedis(MessageListener listener,Long roomId) {
        listenerContainer.removeMessageListener(listener, new ChannelTopic("room/" + roomId));
    }
}
