package com.instargram101.member.service;

import com.instargram101.global.common.exception.customException.CustomException;
import com.instargram101.member.entity.Favorite;
import com.instargram101.member.entity.Member;
import com.instargram101.member.exception.FavoriteErrorCode;
import com.instargram101.member.repoository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService{

    private final FavoriteRepository favoriteRepository;

    @Override
    public Favorite likesStar(Member member, Long starId) {
        if(favoriteRepository.existsByLikerAndStarId(member, starId)){
            throw new CustomException(FavoriteErrorCode.MEMBER_ALREADY_LIKES_STAR);
        }
        Favorite favorite = Favorite.builder()
                .liker(member)
                .starId(starId)
                .build();
        return favoriteRepository.save(favorite);
    }

    @Override
    public List<Favorite> findAllLikesStar(Member member) {
        return favoriteRepository.findAllByLiker(member);
    }

    @Override
    public Long getStarCountsOfMemberLike(Member member) {
        return favoriteRepository.countByLiker(member);
    }

    @Override
    public Long getLikeCountsOfLikeStar(Long starId) {
        return favoriteRepository.countByStarId(starId);
    }

    @Override
    public void dislikesStar(Member member, Long starId) {
        if(!favoriteRepository.existsByLikerAndStarId(member, starId)){
            throw new CustomException(FavoriteErrorCode.MEMBER_ALREADY_DISLIKES_STAR);
        }
        favoriteRepository.deleteAllByLikerAndStarId(member, starId);
    }

}
