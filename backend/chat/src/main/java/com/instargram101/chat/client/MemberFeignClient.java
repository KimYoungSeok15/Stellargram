package com.instargram101.chat.client;

import com.instargram101.chat.dto.request.MemberListFeignRequest;
import com.instargram101.global.common.response.CommonApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient(name = "member", url = "k9a101.p.ssafy.io:8000/member")
public interface MemberFeignClient {
     @PostMapping("/member-list")
    ResponseEntity<CommonApiResponse> getMemberListsById(@RequestBody MemberListFeignRequest request);
}
