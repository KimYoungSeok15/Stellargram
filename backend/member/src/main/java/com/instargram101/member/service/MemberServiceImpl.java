package com.instargram101.member.service;

import com.instargram101.global.common.exception.customException.CustomException;
import com.instargram101.member.dto.request.SignMemberRequestDto;
import com.instargram101.member.entity.Member;
import com.instargram101.member.exception.MemberErrorCode;
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

    public Optional<Member> searchMember(Long memberId) {
        return memberRepository.findByMemberId(memberId);
    }

    public Optional<Member> updateNickname(Long memberId, String nickname) {
        Optional<Member> ismember = memberRepository.findById(memberId);
//                .orElseThrow(() -> new CustomException(MemberErrorCode.Member_Not_Found));
        if (ismember.isPresent()) {
            Member member = ismember.get();
            member.setNickname(nickname);
            memberRepository.save(member);
            return Optional.of(member);
        } else {
            return Optional.empty();
        }
    }
}
