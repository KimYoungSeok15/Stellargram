package com.instargram101.member.api;

import com.instargram101.global.common.response.CommonApiResponse;
import com.instargram101.member.dto.request.SignMemberRequestDto;
import com.instargram101.member.entity.Member;
import com.instargram101.member.service.FollowService;
import com.instargram101.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class MemberController {

    private final MemberService memberServiceImpl;
    private final FollowService followServiceImpl;

    @GetMapping("/check")
    public ResponseEntity<CommonApiResponse<Boolean>> checkById(@RequestHeader("myId") Long memberId) {
        boolean isExist = memberServiceImpl.checkMember(memberId);
        return ResponseEntity.ok(CommonApiResponse.OK(isExist));
    }

    @PostMapping("/signup")
    public ResponseEntity<CommonApiResponse<Member>> createMember(@RequestHeader("myId") Long memberId, @RequestBody SignMemberRequestDto request) {
        var member = memberServiceImpl.createMember(memberId, request);
        return ResponseEntity.ok(CommonApiResponse.OK(member));
    }

    @PostMapping("/check-duplicate")
    public ResponseEntity<CommonApiResponse<Boolean>> checkByNickname(@RequestBody Map<String, Object> request) {
        var nickname = (String) request.get("nickname");
        boolean isSuccess = memberServiceImpl.checkNickname(nickname);
        return ResponseEntity.ok(CommonApiResponse.OK(isSuccess));
    }

    @GetMapping("/me")
    public ResponseEntity<CommonApiResponse<Member>> searchByMemberIdMe(@RequestHeader("myId") Long memberId) {
        var member = memberServiceImpl.searchMember(memberId);
        return ResponseEntity.ok(CommonApiResponse.OK(member));
    }

    @GetMapping("/others/{memberId}")
    public ResponseEntity<CommonApiResponse<Member>> searchByMemberIdOthers(@PathVariable Long memberId) {
        var member = memberServiceImpl.searchMember(memberId);
        return ResponseEntity.ok(CommonApiResponse.OK(member));
    }

    @PatchMapping("/nickname")
    public ResponseEntity<CommonApiResponse<Member>> updateNickname(@RequestHeader("myId") Long memberId, @RequestBody Map<String, Object> request) {
        var nickname = (String) request.get("nickname");
        var member = memberServiceImpl.updateNickname(memberId, nickname);
        return ResponseEntity.ok(CommonApiResponse.OK(member));
    }

    @PatchMapping("/withdrawal")
    public ResponseEntity<CommonApiResponse<Boolean>> deleteMember(@RequestHeader("myId") Long memberId) {
        boolean isSuccess = memberServiceImpl.deleteMember(memberId);
        return ResponseEntity.ok(CommonApiResponse.OK(isSuccess));
    }

    @PostMapping("/follow/{followingId}")
    public ResponseEntity<CommonApiResponse<Boolean>> followUser(@RequestHeader("myId") Long myId, @PathVariable Long followingId) {
        boolean isSuccess = followServiceImpl.followUser(myId, followingId);
        return ResponseEntity.ok(CommonApiResponse.OK(isSuccess));
    }

    @DeleteMapping("/follow/{followingId}")
    public ResponseEntity<CommonApiResponse<Boolean>> deleteFollow(@RequestHeader("myId") Long myId, @PathVariable Long followingId) {
        boolean isSuccess = followServiceImpl.deleteFollow(myId, followingId);
        return ResponseEntity.ok(CommonApiResponse.OK(isSuccess));
    }

    @GetMapping("/follow/{memberId}")
    public ResponseEntity<CommonApiResponse<List<Member>>> getFollowers(@PathVariable Long memberId) {
        List<Member> followers = followServiceImpl.getFollowers(memberId);
        return ResponseEntity.ok(CommonApiResponse.OK(followers));
    }

    @GetMapping("/following/{memberId}")
    public ResponseEntity<CommonApiResponse<List<Member>>> getFollowing(@PathVariable Long memberId) {
        List<Member> followingMembers = followServiceImpl.getFollowingMembers(memberId);
        return ResponseEntity.ok(CommonApiResponse.OK(followingMembers));
    }

    @PostMapping("/id")
    public ResponseEntity<CommonApiResponse<Long>> getMemberIdByNickname(@RequestBody Map<String, Object> request) {
        var nickname = (String) request.get("nickname");
        Long memberId = memberServiceImpl.getMemberIdByNickname(nickname);
        return ResponseEntity.ok(CommonApiResponse.OK(memberId));
    }

    @PostMapping("/nickname/search")
    public ResponseEntity<CommonApiResponse<List<Member>>> searchMembersByNickname(@RequestBody Map<String, Object> request) {
        var searchNickname = (String) request.get("searchNickname");
        List<Member> members = memberServiceImpl.searchMembersByNickname(searchNickname);
        return ResponseEntity.ok(CommonApiResponse.OK(members));
    }

    @PatchMapping("/profile-image")
    public ResponseEntity<CommonApiResponse<Member>> updateProfileImage(@RequestHeader("myId") Long myId, MultipartFile profileImageFile) throws IOException {
        var member =  memberServiceImpl.updateProfileImage(myId, profileImageFile);
        return ResponseEntity.ok(CommonApiResponse.OK(member));
    }


    @GetMapping("/like-member/{cardId}")
    public ResponseEntity<CommonApiResponse<List<Member>>> getMembersByCardId(@PathVariable Long cardId) {
        List<Long> memberIds = memberServiceImpl.getMemberIdsByCardId(cardId);
        List<Member> members = memberServiceImpl.getMembersByMemberIds(memberIds);
        return ResponseEntity.ok(CommonApiResponse.OK(members));
    }
}
