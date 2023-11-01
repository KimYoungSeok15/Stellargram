package com.instargram101.member.repoository;

import com.instargram101.member.entity.Follow;
import com.instargram101.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Long> findFollowerIdsByFollowingId(Long followingId);
    @Query("SELECT f from Follow as f where f.follower.memberId = :followerId and f.followee.memberId = :followeeId")
    Optional<Follow> findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);


}
