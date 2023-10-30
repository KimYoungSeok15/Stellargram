package com.instargram101.member.api;

import com.instargram101.global.common.response.CommonApiResponse;
import com.instargram101.member.dto.request.NicknameRequestDto;
import com.instargram101.member.dto.request.SignMemberRequestDto;
import com.instargram101.member.dto.response.SampleResponse;
import com.instargram101.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class MemberController {

    private final MemberService memberServiceImpl;

    @GetMapping("/test")
    public ResponseEntity<CommonApiResponse> sampleController(){ //ResponseEntity로 안 감싸줘도 됨.

        var data = SampleResponse.builder()
                .status("OK")
                .build();

        //var response = CommonApiResponse.OK(data); // 기본 message "ok" 출력.
        var response = CommonApiResponse.OK("sample message", data); //넣고 싶은 데이터를 넣으면 된다.
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check")
    public ResponseEntity<CommonApiResponse<Boolean>> checkById(@RequestHeader("myId") Long memberId) {
        boolean isExist = memberServiceImpl.checkMember(memberId);
        var response = CommonApiResponse.OK(isExist);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<CommonApiResponse> createMember(@RequestHeader("myId") Long memberId, @RequestBody SignMemberRequestDto request) {
        var member = memberServiceImpl.createMember(memberId, request);
        var response = CommonApiResponse.OK(member);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-duplicate")
    public ResponseEntity<CommonApiResponse<Boolean>> checkByNickname(@RequestBody NicknameRequestDto request) {
        boolean isSuccess = memberServiceImpl.checkNickname(request.getNickname());
        var response = CommonApiResponse.OK(isSuccess);
        return ResponseEntity.ok(response);
    }
}
