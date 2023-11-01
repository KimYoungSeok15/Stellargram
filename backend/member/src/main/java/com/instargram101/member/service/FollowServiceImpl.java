package com.instargram101.member.service;

import com.instargram101.global.common.exception.customException.CustomException;
import com.instargram101.member.entity.Follow;
import com.instargram101.member.entity.Member;
import com.instargram101.member.exception.MemberErrorCode;
import com.instargram101.member.repoository.FollowRepository;
import com.instargram101.member.repoository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    public Boolean followUser(Long follower, Long followee) {
        Member myInfo = memberRepository.findById(follower)
                .orElseThrow(() -> new CustomException(MemberErrorCode.Member_Not_Found));
        Member followingInfo = memberRepository.findById(followee)
                .orElseThrow(() -> new CustomException(MemberErrorCode.Member_Not_Found));

        followRepository.save(Follow.builder().followee(followingInfo).follower(myInfo).build());
        setFollowingCount(myInfo, 1);
        setFollowCount(followingInfo, 1);
        return true;
    }

    public void setFollowingCount(Member member, int count) {
        member.setFollowingCount(member.getFollowingCount() + 1);
        memberRepository.save(member);
    }

    public void setFollowCount(Member member, int count) {
        member.setFollowCount(member.getFollowCount() + 1);
        memberRepository.save(member);
    }
}
