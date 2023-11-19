package com.instargram101.starcard.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.instargram101.starcard.entity.Starcard;
import com.instargram101.starcard.entity.StarcardLike;
import com.instargram101.starcard.repoository.StarcardLikeRepository;
import com.instargram101.starcard.repoository.StarcardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final StarcardRepository starcardRepository;
    private final StarcardLikeRepository starcardLikeRepository;

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

        List<Starcard> res = starcardRepository.findAllByMemberId(Long.valueOf(String.valueOf(map.get("memberId"))));
        List<StarcardLike> res2 = starcardLikeRepository.findAllByMemberId(Long.valueOf(String.valueOf(map.get("memberId"))));

        starcardRepository.deleteAll(res);
        starcardLikeRepository.deleteAll(res2);
    }
}