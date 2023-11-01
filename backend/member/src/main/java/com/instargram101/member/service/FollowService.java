package com.instargram101.member.service;

import com.instargram101.member.entity.Member;

import java.util.List;

public interface FollowService {
    Boolean followUser(Long follower, Long followee);
    Boolean deleteFollow(Long followerId, Long followeeId);
    void setFollowingCount(Member member, int count);
    void setFollowCount(Member member, int count);
    List<Member> getFollowers(Long memberId);
    List<Member> getFollowingMembers(Long memberId);
}
