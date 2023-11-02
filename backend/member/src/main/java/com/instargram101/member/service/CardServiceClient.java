package com.instargram101.member.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(name = "card-service", url = "k9a101.p.ssafy.io:8000/starcard")
public interface CardServiceClient {

    @GetMapping("/card/like-member/{cardId}")
    List<Long> getMemberIdsByCardId(@PathVariable("cardId") Long cardId);

}