package com.instargram101;

import com.instargram101.chat.entity.ChatRoom;
import com.instargram101.chat.repository.ChatRoomRepository;
import com.instargram101.chat.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ChatApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageListenerAdapter listenerAdapter;
    private final TopicService topicService;

    private boolean executed = false;

    // 인메모리인 레디스 서버 실행 시 레디스 리스너 컨테이너에 비어있지 않은 방에 대한 토픽 등록
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!executed) {
            List<ChatRoom> notEmptyRooms = chatRoomRepository.findChatRoomNotEmpty();
            for(ChatRoom room:notEmptyRooms){
                topicService.addRoomTopicToRedis(listenerAdapter,room.getRoomId());
            }
        }
        executed = true;

    }
}
