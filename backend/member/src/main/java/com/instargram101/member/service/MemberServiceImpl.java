package com.instargram101.member.service;

import com.instargram101.global.common.exception.customException.CustomException;
import com.instargram101.member.dto.request.SignMemberRequestDto;
import com.instargram101.member.entity.Member;
import com.instargram101.member.exception.MemberErrorCode;
import com.instargram101.member.repoository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.member;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public Boolean checkMember(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        return !member.isEmpty();

    }

    public Member createMember(Long memberId, SignMemberRequestDto request) {
        String nickname = request.getNickname();
        String profileImageUrl = request.getProfileImageUrl();

        Member member = Member.builder()
                .memberId(memberId)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .activated(true)
                .followCount(0L)
                .followingCount(0L)
                .cardCount(0L)
                .build();
        return memberRepository.save(member);
    }

    public Boolean checkNickname(String nickname) {
        return !memberRepository.existsByNickname(nickname);
    }

    public Member searchMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.Member_Not_Found));
    }


    public Member updateNickname(Long memberId, String nickname) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.Member_Not_Found));
        member.setNickname(nickname);
        return memberRepository.save(member);

    }

    public Boolean deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.Member_Not_Found));
        member.setActivated(false);
        memberRepository.save(member);
        return true;
    }
}
