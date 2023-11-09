package com.instargram101.chat.service;

import org.springframework.data.redis.connection.MessageListener;

public interface TopicService {

    // 토픽 추가
    public void addRoomTopicToRedis(MessageListener listener, Long roomId);

    // 토픽 삭제
    public void removeRoomTopicToRedis(MessageListener listener,Long roomId);
}
