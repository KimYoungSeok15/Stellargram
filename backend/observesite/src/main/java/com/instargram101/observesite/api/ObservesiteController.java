package com.instargram101.observesite.api;

import com.instargram101.global.common.response.CommonApiResponse;
import com.instargram101.observesite.dto.response.SampleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class ObservesiteController {

//    private final MemberService memberService;

    @GetMapping("/test")
    public ResponseEntity<CommonApiResponse> sampleController(){ //ResponseEntity로 안 감싸줘도 됨.

        var data = SampleResponse.builder()
                .status("OK")
                .build();

        //var response = CommonApiResponse.OK(data); // 기본 message "ok" 출력.
        var response = CommonApiResponse.OK("sample message", data); //넣고 싶은 데이터를 넣으면 된다.
        return ResponseEntity.ok(response);
    }
}

