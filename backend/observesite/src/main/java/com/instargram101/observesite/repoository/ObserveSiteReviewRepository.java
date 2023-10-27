package com.instargram101.observesite.repoository;

import com.instargram101.observesite.entity.ObserveSite;
import com.instargram101.observesite.entity.ObserveSiteReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObserveSiteReviewRepository extends JpaRepository<ObserveSiteReview, Long> {

    //하나의 관측지 내에 해당 memberId가 작성한 적이 있는가?
    boolean existsByMemberIdAndObserveSite(Long memberId, ObserveSite observeSite);
}
