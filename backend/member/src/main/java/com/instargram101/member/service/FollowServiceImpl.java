package com.instargram101.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    public boolean followUser(Long myId, Long followingId) {
        followRepository.save(new Follow(myId, followingId));

        Member myInfo = memberRepository.findById(myId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.Member_Not_Found));
        myInfo.setFollowingCount(myInfo.getFollowingCount() + 1);
        memberRepository.save(myInfo);

        Member followingInfo = memberRepository.findById(followingId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.Member_Not_Found));
        followingInfo.setFollowerCount(followingInfo.getFollowerCount() + 1);
        memberRepository.save(followingInfo);

        return true;
    }
}
