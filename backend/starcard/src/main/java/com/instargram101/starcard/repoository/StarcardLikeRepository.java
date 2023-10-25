package com.instargram101.starcard.repoository;

import com.instargram101.starcard.entity.StarcardLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StarcardLikeRepository extends JpaRepository<StarcardLike, Long> {
}
