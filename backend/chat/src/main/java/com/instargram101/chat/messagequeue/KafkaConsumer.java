package com.instargram101.chat.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.instargram101.chat.entity.ChatJoin;
import com.instargram101.chat.entity.ChatRoom;
import com.instargram101.chat.repository.ChatJoinRepository;
import com.instargram101.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final ChatJoinRepository chatJoinRepository;
    private final ChatRoomRepository chatRoomRepository;

    @KafkaListener(topics= "test-topic")
    public void deleteMember(String kafkaMessage) {

        log.info("Kafka Message: ->" + kafkaMessage);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {
            });
        } catch (JsonProcessingException ex){
            ex.printStackTrace();
        }

        Long memberId = Long.valueOf(String.valueOf(map.get("memberId")));

        List<ChatJoin> chatJoins = chatJoinRepository.findAllByMemberId(memberId);
        for (ChatJoin chatJoin: chatJoins){
            Optional<ChatRoom> chatRoom = chatRoomRepository.findById(chatJoin.getRoom().getRoomId());
            chatRoom.ifPresent(chatRoom1 -> {
                chatRoom1.setPersonnel(chatRoom1.getPersonnel()-1);
                chatRoomRepository.save(chatRoom1);
            });
        }
        chatJoinRepository.deleteAll(chatJoins);
    }
}