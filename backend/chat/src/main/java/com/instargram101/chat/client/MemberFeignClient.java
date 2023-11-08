package com.instargram101.chat.client;
import com.instargram101.global.common.response.CommonApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient(name = "member", url = "k9a101.p.ssafy.io:8000/member")
public interface MemberFeignClient {
    @GetMapping("/member-list") //endpoint 설정, 이는 JPA 사용과 너무 똑같다.
    ResponseEntity<CommonApiResponse> getMemberListsById(@RequestBody List<Long> memberIds);
}
