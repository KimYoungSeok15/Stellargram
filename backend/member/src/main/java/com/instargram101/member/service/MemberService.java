package com.instargram101.member.service;

import com.instargram101.member.dto.request.SignMemberRequestDto;
import com.instargram101.member.entity.Member;

import java.util.Optional;

public interface MemberService {

    Boolean checkMember(Long memberId);
    Member createMember(Long memberId, SignMemberRequestDto request);
    Boolean checkNickname(String nickname);
    Member searchMember(Long memberId);
    Member updateNickname(Long memberId, String nickname);
    Boolean deleteMember(Long memberId);
    Long getMemberIdByNickname(String nickname);

}
