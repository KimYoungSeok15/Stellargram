package com.instargram101.member.service;

import com.instargram101.member.entity.Member;
import com.instargram101.member.repoository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public Boolean checkMember(Long memberId) {
        Optional<Member> member = memberRepository.findByMemberId(memberId);
        return !member.isEmpty();

    }
}
