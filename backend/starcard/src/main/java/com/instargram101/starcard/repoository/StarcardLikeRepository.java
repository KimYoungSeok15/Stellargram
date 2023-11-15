package com.instargram101.starcard.repoository;

import com.instargram101.starcard.entity.StarcardLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StarcardLikeRepository extends JpaRepository<StarcardLike, Long> {

    @Query("SELECT sl FROM StarcardLike sl WHERE sl.memberId =:myId AND sl.card.cardId =:cardId")
    Optional<StarcardLike> findByMemberIdAndCardId(@Param("myId") Long myId, @Param("cardId") Long cardId);

    @Query("SELECT sl FROM StarcardLike sl WHERE sl.memberId =:memberId")
    List<StarcardLike> findAllByMemberId(@Param("memberId") Long memberId);
}
