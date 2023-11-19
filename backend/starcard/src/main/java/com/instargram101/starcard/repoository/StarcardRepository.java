package com.instargram101.starcard.repoository;

import com.instargram101.starcard.dto.query.StarcardWithAmILikeQueryDto;
import com.instargram101.starcard.entity.Starcard;
import com.instargram101.starcard.entity.enums.StarcardCategory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StarcardRepository extends JpaRepository<Starcard, Long> {

    List<Starcard> searchAllByMemberId(Long memberId);

    @Query("SELECT NEW com.instargram101.starcard.dto.query.StarcardWithAmILikeQueryDto(sc.cardId, sc.memberId, sc.observeSiteId, sc.imagePath, sc.imageUrl, sc.content, sc.photoAt, sc.category, sc.tools, sc.likeCount, CASE WHEN sl.likeId IS NOT NULL THEN true ELSE false END) " +
            "FROM Starcard sc " +
            "LEFT JOIN sc.starcardLikes sl ON sl.memberId = :myId " +
            "WHERE sc.memberId = :memberId")
    List<StarcardWithAmILikeQueryDto> findAllCardsWithLikeStatus(@Param("myId") Long myId, @Param("memberId") Long memberId);

    @Query("SELECT NEW com.instargram101.starcard.dto.query.StarcardWithAmILikeQueryDto(sc.cardId, sc.memberId, sc.observeSiteId, sc.imagePath, sc.imageUrl, sc.content, sc.photoAt, sc.category, sc.tools, sc.likeCount, CASE WHEN sl.likeId IS NOT NULL THEN true ELSE false END) " +
            "FROM Starcard sc " +
            "LEFT JOIN sc.starcardLikes sl ON sl.memberId = :myId " +
            "WHERE sc.content LIKE %:keyword% AND sc.category = :category")
    List<StarcardWithAmILikeQueryDto> findByKeywordAndCategory(@Param("myId") Long myId, @Param("keyword") String keyword, @Param("category") StarcardCategory category);

    @Query("SELECT NEW com.instargram101.starcard.dto.query.StarcardWithAmILikeQueryDto(sc.cardId, sc.memberId, sc.observeSiteId, sc.imagePath, sc.imageUrl, sc.content, sc.photoAt, sc.category, sc.tools, sc.likeCount, CASE WHEN sl.likeId IS NOT NULL THEN true ELSE false END) " +
            "FROM Starcard sc " +
            "LEFT JOIN sc.starcardLikes sl ON sl.memberId = :myId " +
            "WHERE sc.cardId IN (SELECT sc.cardId from Starcard sc JOIN sc.starcardLikes sl WHERE sl.memberId =:memberId )")
    List<StarcardWithAmILikeQueryDto> findCardsLikedByUser(@Param("myId") Long myId, @Param("memberId") Long memberId);

    @Query("SELECT sl.memberId FROM StarcardLike sl WHERE sl.card.cardId =:cardId ")
    List<Long> findLikedMembersByCardId(@Param("cardId") Long cardId);

    @Query("SELECT NEW com.instargram101.starcard.dto.query.StarcardWithAmILikeQueryDto(sc.cardId, sc.memberId, sc.observeSiteId, sc.imagePath, sc.imageUrl, sc.content, sc.photoAt, sc.category, sc.tools, sc.likeCount, CASE WHEN sl.likeId IS NOT NULL THEN true ELSE false END) " +
            "FROM Starcard sc " +
            "LEFT JOIN sc.starcardLikes sl ON sl.memberId = :myId " +
            "WHERE sc.likeCount > 9 " +
            "ORDER BY RAND() ")
    StarcardWithAmILikeQueryDto findOneRandomly(@Param("myId") Long myId, PageRequest pageRequest);

    @Query("DELETE FROM Starcard sc " +
            "WHERE sc.memberId = :memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT sc FROM Starcard sc WHERE sc.memberId =:memberId")
    List<Starcard> findAllByMemberId(@Param("memberId") Long memberId);
}