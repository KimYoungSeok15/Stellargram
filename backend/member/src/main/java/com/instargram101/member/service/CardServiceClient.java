package com.instargram101.member.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "card-service", url = "k9a101.p.ssafy.io:3307")
public interface CardServiceClient {

    @GetMapping("/cards/like-member/{cardId}")
    List<Long> getMemberIdsByCardId(@PathVariable("cardId") Long cardId);
}