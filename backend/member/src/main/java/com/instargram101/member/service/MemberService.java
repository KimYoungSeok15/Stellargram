package com.instargram101.member.service;

import com.instargram101.member.dto.request.SignMemberRequestDto;
import com.instargram101.member.entity.Member;

import java.util.Optional;

public interface MemberService {

    Boolean checkMember(Long memberId);
    Member createMember(Long memberId, SignMemberRequestDto request);
    Boolean checkNickname(String nickname);
    Optional<Member> searchMember(Long memberId);
    Optional<Member> updateNickname(Long memberId, String nickname);
}
