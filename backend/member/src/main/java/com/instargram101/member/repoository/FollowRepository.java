package com.instargram101.member.repoository;

import com.instargram101.member.entity.Follow;
import com.instargram101.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    @Query("SELECT f from Follow as f where f.follower.memberId = :followerId and f.followee.memberId = :followeeId")
    Optional<Follow> findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    @Query("SELECT f.follower FROM Follow AS f WHERE f.followee.memberId = :followingId")
    List<Member> findFollowersByFollowingId(@Param("followingId") Long followingId);


    @Query("SELECT f.followee from Follow as f where f.follower.memberId = :followerId")
    List<Member> findFollowingMembersByFollowerId(Long followerId);

}
