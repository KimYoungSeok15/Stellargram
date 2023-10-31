package com.instargram101.member.repoository;

import com.instargram101.member.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Long> findFollowerIdsByFollowingId(Long followingId);
}
