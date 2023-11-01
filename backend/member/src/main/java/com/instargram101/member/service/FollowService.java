package com.instargram101.member.service;

import com.instargram101.member.entity.Member;

public interface FollowService {
    Boolean followUser(Long follower, Long followee);
    Boolean deleteFollow(Long followerId, Long followeeId);
    public void setFollowingCount(Member member, int count);
    public void setFollowCount(Member member, int count);

}
