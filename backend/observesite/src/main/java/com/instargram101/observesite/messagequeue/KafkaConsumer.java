package com.instargram101.observesite.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.instargram101.observesite.entity.ObserveSite;
import com.instargram101.observesite.entity.ObserveSiteReview;
import com.instargram101.observesite.repoository.ObserveSiteRepository;
import com.instargram101.observesite.repoository.ObserveSiteReviewRepository;
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

    private final ObserveSiteRepository observeSiteRepository;
    private final ObserveSiteReviewRepository observeSiteReviewRepository;

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

        List<ObserveSiteReview> observeSiteReviews = observeSiteReviewRepository.findAllByMemberId(memberId);
        for(ObserveSiteReview observeSiteReview : observeSiteReviews){
            Optional<ObserveSite> observeSite = observeSiteRepository.findById(observeSiteReview.getObserveSite().getObserveSiteId());
            observeSite.ifPresent(observeSite1 -> {
                observeSite1.setReviewCount(observeSite1.getReviewCount()-1);
                observeSiteRepository.save(observeSite1);
            });
        }
        observeSiteReviewRepository.deleteAll(observeSiteReviews);


    }
}