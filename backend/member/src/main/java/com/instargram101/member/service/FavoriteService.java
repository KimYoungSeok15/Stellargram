package com.instargram101.member.service;

import com.instargram101.member.entity.Favorite;
import com.instargram101.member.entity.Member;

import java.util.List;


public interface FavoriteService {

    Favorite likesStar(Member member, Long starId);

    List<Favorite> findAllLikesStar(Member member);

    Long getStarCountsOfMemberLike(Member member);

    Long getLikeCountsOfLikeStar(Long starId);

    void dislikesStar(Member member, Long starId);
}
