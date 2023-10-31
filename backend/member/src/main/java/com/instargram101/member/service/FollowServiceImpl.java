package com.instargram101.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    public List<Member> getFollowers(Long memberId) {
        List<Long> followerIds = followRepository.findFollowerIdsByFollowingId(memberId);
        List<Member> followers = memberRepository.findAllById(followerIds);
        return followers;
    }
}
