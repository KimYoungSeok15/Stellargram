package com.instargram101.member.repoository;

import com.instargram101.member.entity.Follow;
import com.instargram101.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.servlet.tags.form.SelectTag;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    @Query("SELECT f from Follow as f where f.follower.memberId = :followerId and f.followee.memberId = :followeeId")
    Optional<Follow> findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);
    @Query("SELECT f.follower.memberId from Follow as f where f.followee.memberId = :followingId")
    List<Long> findFollowerIdsByFollowingId(Long followingId);

}
