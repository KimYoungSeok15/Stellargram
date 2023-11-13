package com.instargram101.observesite.service;

import com.instargram101.observesite.entity.ObserveSite;
import com.instargram101.observesite.entity.ObserveSiteReview;

import java.util.List;

public interface ObserveSiteReviewService {
    //observesiteId를 이용하여 ObserveSite 연결하고 createdAt 추가 하고 save하기
    ObserveSiteReview createReview(ObserveSiteReview review, ObserveSite observeSite, Long memberId);

    //관측지, memberId로 작성한 적이 있는지 조회
    boolean checkCommentExists(ObserveSite observeSite, Long memberId);

    List<ObserveSiteReview> getReviews(ObserveSite observeSite);
}
