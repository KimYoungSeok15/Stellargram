package com.instargram101.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    public List<Member> getFollowingMembers(Long memberId) {
        List<Long> followingIds = followRepository.findFollowingIdsByFollowerId(memberId);
        List<Member> followingMembers = memberRepository.findAllById(followingIds);
        return followingMembers;
    }
}
