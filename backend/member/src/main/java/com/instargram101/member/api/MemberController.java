package com.instargram101.member.api;

import com.instargram101.global.common.exception.customException.CustomException;
import com.instargram101.global.common.response.CommonApiResponse;
import com.instargram101.member.dto.request.SignMemberRequestDto;
import com.instargram101.member.dto.response.SampleResponse;
import com.instargram101.member.entity.Member;
import com.instargram101.member.exception.MemberErrorCode;
import com.instargram101.member.service.FollowService;
import com.instargram101.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class MemberController {

    private final MemberService memberServiceImpl;
    private final FollowService followServiceImpl;

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
        return ResponseEntity.ok(CommonApiResponse.OK(isExist));
    }

    @PostMapping("/signup")
    public ResponseEntity<CommonApiResponse> createMember(@RequestHeader("myId") Long memberId, @RequestBody SignMemberRequestDto request) {
        var member = memberServiceImpl.createMember(memberId, request);
        return ResponseEntity.ok(CommonApiResponse.OK(member));
    }

    @GetMapping("/check-duplicate")
    public ResponseEntity<CommonApiResponse<Boolean>> checkByNickname(@RequestBody Map<String, Object> request) {
        var nickname = (String) request.get("nickname");
        boolean isSuccess = memberServiceImpl.checkNickname(nickname);
        return ResponseEntity.ok(CommonApiResponse.OK(isSuccess));
    }

    @GetMapping("/me")
    public ResponseEntity<CommonApiResponse> searchByMemberIdMe(@RequestHeader("myId") Long memberId) {
        var member = memberServiceImpl.searchMember(memberId);
        return ResponseEntity.ok(CommonApiResponse.OK(member));
    }

    @GetMapping("/others/{memberId}")
    public ResponseEntity<CommonApiResponse> searchByMemberIdOthers(@PathVariable Long memberId) {
        var member = memberServiceImpl.searchMember(memberId);
        return ResponseEntity.ok(CommonApiResponse.OK(member));
    }

    @PatchMapping("/nickname")
    public ResponseEntity<CommonApiResponse> updateNickname(@RequestHeader("myId") Long memberId, @RequestBody Map<String, Object> request) {
        var nickname = (String) request.get("nickname");
        var member = memberServiceImpl.updateNickname(memberId, nickname);
        return ResponseEntity.ok(CommonApiResponse.OK(member));
    }

    @PatchMapping("/withdrawal")
    public ResponseEntity<CommonApiResponse> deleteMember(@RequestHeader("myId") Long memberId) {
        boolean isSuccess = memberServiceImpl.deleteMember(memberId);
        return ResponseEntity.ok(CommonApiResponse.OK(isSuccess));
    }

    @PostMapping("/follow/{followingId}")
    public ResponseEntity<CommonApiResponse> followUser(@RequestHeader("myId") Long myId, @PathVariable Long followingId) {
        boolean isSuccess = followServiceImpl.followUser(myId, followingId);
        return ResponseEntity.ok(CommonApiResponse.OK(isSuccess));
    }

    @DeleteMapping("/follow/{followingId}")
    public ResponseEntity<CommonApiResponse> deleteFollow(@RequestHeader("myId") Long myId, @PathVariable Long followingId) {
        boolean isSuccess = followServiceImpl.deleteFollow(myId, followingId);
        return ResponseEntity.ok(CommonApiResponse.OK(isSuccess));
    }

    @GetMapping("/follow/{memberId}")
    public ResponseEntity<CommonApiResponse> getFollowers(@PathVariable Long memberId) {
        List<Member> followers = followServiceImpl.getFollowers(memberId);
        return ResponseEntity.ok(CommonApiResponse.OK(followers));
    }

    @GetMapping("/following/{memberId}")
    public ResponseEntity<CommonApiResponse> getFollowing(@PathVariable Long memberId) {
        List<Member> followingMembers = followServiceImpl.getFollowingMembers(memberId);
        return ResponseEntity.ok(CommonApiResponse.OK(followingMembers));
    }

    @GetMapping("/id")
    public ResponseEntity<CommonApiResponse> getMemberIdByNickname(@RequestBody Map<String, Object> request) {
        var nickname = (String) request.get("nickname");
        Long memberId = memberServiceImpl.getMemberIdByNickname(nickname);
        return ResponseEntity.ok(CommonApiResponse.OK(memberId));
    }
}
