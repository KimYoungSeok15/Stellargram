package com.instargram101.member.repoository;

import com.instargram101.member.entity.Favorite;
import com.instargram101.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Boolean existsByLikerAndStarId(Member member, Long starId);

    List<Favorite> findAllByLiker(Member member);

    Long countByLiker(Member member);

    Long countByStarId(Long starId);

    @Transactional
    void deleteAllByLikerAndStarId(Member member, Long starId);
}

