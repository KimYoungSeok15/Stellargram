package com.instargram101.member.api;

import com.instargram101.global.common.response.CommonApiResponse;
import com.instargram101.member.dto.request.SignMemberRequestDto;
import com.instargram101.member.dto.request.findMemberListRequestDto;
import com.instargram101.member.dto.response.*;
import com.instargram101.member.entity.Member;
import com.instargram101.member.service.FollowService;
import com.instargram101.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
@Controller
@EnableAspectJAutoProxy
public class MemberController {

    private final MemberService memberServiceImpl;
    private final FollowService followServiceImpl;

    @GetMapping("/check")
    public ResponseEntity<CommonApiResponse<StatusResponse>> checkById(@RequestHeader("myId") Long memberId) {
        StatusResponse response = StatusResponse.builder().status(memberServiceImpl.checkMember(memberId)).build();
        return ResponseEntity.ok(CommonApiResponse.OK(response));
    }

    @PostMapping("/signup")
    public ResponseEntity<CommonApiResponse<Member>> createMember(@RequestHeader("myId") Long memberId, @RequestBody SignMemberRequestDto request) {
        return ResponseEntity.ok(CommonApiResponse.OK(memberServiceImpl.createMember(memberId, request)));
    }

    @PostMapping("/check-duplicate")
    public ResponseEntity<CommonApiResponse<StatusResponse>> checkByNickname(@RequestBody Map<String, Object> request) {
        var nickname = (String) request.get("nickname");
        StatusResponse response = StatusResponse.builder().status(memberServiceImpl.checkNickname(nickname)).build();
        return ResponseEntity.ok(CommonApiResponse.OK(response));
    }

    @GetMapping("/me")
    public ResponseEntity<CommonApiResponse<Member>> searchByMemberIdMe(@RequestHeader("myId") Long memberId) {
        return ResponseEntity.ok(CommonApiResponse.OK(memberServiceImpl.searchMember(memberId)));
    }

    @GetMapping("/others/{memberId}")
    public ResponseEntity<CommonApiResponse<FindMemberResponseDto>> searchByMemberIdOthers(@RequestHeader("myId") Long myId, @PathVariable Long memberId) {
        FindMemberResponseDto response = followServiceImpl.findFollow(myId, memberId);
        return ResponseEntity.ok(CommonApiResponse.OK(response));
    }

    @PatchMapping("/nickname")
    public ResponseEntity<CommonApiResponse<Member>> updateNickname(@RequestHeader("myId") Long memberId, @RequestBody Map<String, Object> request) {
        var nickname = (String) request.get("nickname");
        return ResponseEntity.ok(CommonApiResponse.OK(memberServiceImpl.updateNickname(memberId, nickname)));
    }

    @PatchMapping("/profile-image")
    public ResponseEntity<CommonApiResponse<Member>> updateProfileImage(@RequestHeader("myId") Long myId, MultipartFile profileImageFile) throws IOException {
        return ResponseEntity.ok(CommonApiResponse.OK(memberServiceImpl.updateProfileImage(myId, profileImageFile)));
    }

    @PatchMapping("/withdrawal")
    public ResponseEntity<CommonApiResponse<StatusResponse>> deleteMember(@RequestHeader("myId") Long memberId) {
        StatusResponse response = StatusResponse.builder().status(memberServiceImpl.deleteMember(memberId)).build();
        return ResponseEntity.ok(CommonApiResponse.OK(response));
    }

    @GetMapping ("/follow/{followingId}")
    public ResponseEntity<CommonApiResponse<StatusResponse>> followUser(@RequestHeader("myId") Long myId, @PathVariable Long followingId) {
        StatusResponse response = StatusResponse.builder().status(followServiceImpl.followUser(myId, followingId)).build();
        return ResponseEntity.ok(CommonApiResponse.OK(response));
    }

    @DeleteMapping("/follow/{followingId}")
    public ResponseEntity<CommonApiResponse<StatusResponse>> deleteFollow(@RequestHeader("myId") Long myId, @PathVariable Long followingId) {
        StatusResponse response = StatusResponse.builder().status(followServiceImpl.deleteFollow(myId, followingId)).build();
        return ResponseEntity.ok(CommonApiResponse.OK(response));
    }

    @GetMapping("/follow-list/{memberId}")
    public ResponseEntity<CommonApiResponse<MemberListResponse>> getFollowers(@PathVariable Long memberId) {
        MemberListResponse response = MemberListResponse.builder().members(followServiceImpl.getFollowers(memberId)).build();
        return ResponseEntity.ok(CommonApiResponse.OK(response));
    }

    @GetMapping("/following-list/{memberId}")
    public ResponseEntity<CommonApiResponse<MemberListResponse>> getFollowing(@PathVariable Long memberId) {
        MemberListResponse response = MemberListResponse.builder().members(followServiceImpl.getFollowingMembers(memberId)).build();
        return ResponseEntity.ok(CommonApiResponse.OK(response));
    }

    @PostMapping("/id")
    public ResponseEntity<CommonApiResponse<MemberIdResponse>> getMemberIdByNickname(@RequestBody Map<String, Object> request) {
        var nickname = (String) request.get("nickname");
        MemberIdResponse response = MemberIdResponse.builder().memberId(memberServiceImpl.getMemberIdByNickname(nickname)).build();
        return ResponseEntity.ok(CommonApiResponse.OK(response));
    }

    @PostMapping("/nickname/search")
    public ResponseEntity<CommonApiResponse<FindMemberListResponse>> searchMembersByNickname(@RequestHeader("myId") Long myId, @RequestBody Map<String, Object> request) {
        var searchNickname = (String) request.get("searchNickname");
        var memberList = memberServiceImpl.searchMembersByNickname(searchNickname);
        FindMemberListResponse response = FindMemberListResponse.builder().members(followServiceImpl.findMembers(myId, memberList)).build();
        return ResponseEntity.ok(CommonApiResponse.OK(response));
    }

    @GetMapping("/like-member/{cardId}")
    public ResponseEntity<CommonApiResponse<MemberListResponse>> getMembersByCardId(@PathVariable Long cardId) {
        List<Long> memberIds = memberServiceImpl.getMemberIdsByCardId(cardId);
        MemberListResponse response = MemberListResponse.builder().members(memberServiceImpl.getMembersByMemberIds(memberIds)).build();
        return ResponseEntity.ok(CommonApiResponse.OK(response));
    }

    @PostMapping("/member-list")
    public ResponseEntity<CommonApiResponse<MemberListResponse>> getMembersByMemberId(@RequestBody findMemberListRequestDto request) {
        MemberListResponse response = MemberListResponse.builder().members(memberServiceImpl.getMembersByMemberIds(request.getMemberIds())).build();
        return ResponseEntity.ok(CommonApiResponse.OK(response));
    }
}
