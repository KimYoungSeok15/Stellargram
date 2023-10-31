package com.instargram101.member.service;

public interface FollowService {
    Boolean followUser(Long myId, Long followingId);
}
