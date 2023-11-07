package com.instargram101.member.service;

import com.instargram101.global.common.response.CommonApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "card-service", url = "k9a101.p.ssafy.io:8000/starcard")
public interface CardServiceClient {

    @GetMapping("/card/like-member/{cardId}")
    ResponseEntity<CommonApiResponse> getMemberIdsByCardId(@PathVariable("cardId") Long cardId);

}