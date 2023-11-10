package com.instargram101.starcard.service;

import com.instargram101.global.common.response.CommonApiResponse;
import com.instargram101.starcard.dto.request.MemberClientReqeustDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name="MemberServiceClient", url = "http://k9a101.p.ssafy.io:8000")
public interface MemberServiceClient {
    @PostMapping("/member/member-list")
    ResponseEntity<CommonApiResponse> getMemberInfoByMemberIds(
            @RequestBody MemberClientReqeustDto memberClientReqeustDto
            );
}