package com.instargram101.member.service;

import com.instargram101.member.dto.request.SignMemberRequestDto;
import com.instargram101.member.entity.Member;

public interface MemberService {

    Boolean checkMember(Long memberId);
    Member createMember(Long memberId, SignMemberRequestDto request);
}
