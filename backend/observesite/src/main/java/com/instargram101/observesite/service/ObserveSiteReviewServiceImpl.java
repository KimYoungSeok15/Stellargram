package com.instargram101.observesite.service;

import com.instargram101.observesite.entity.ObserveSite;
import com.instargram101.observesite.entity.ObserveSiteReview;
import com.instargram101.observesite.repoository.ObserveSiteReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ObserveSiteReviewServiceImpl implements ObserveSiteReviewService{

    private final ObserveSiteReviewRepository observeSiteReviewRepository;

    //observesiteId를 이용하여 ObserveSite 연결하고 createdAt 추가 하고 save하기
    @Override
    public ObserveSiteReview createReview(ObserveSiteReview review, ObserveSite observeSite, Long memberId){
        review.setObserveSite(observeSite);
        review.setCreatedAt(LocalDateTime.now());
        review.setMemberId(memberId);
        return observeSiteReviewRepository.save(review);
    }

    //관측지, memberId로 작성한 적이 있는지 조회
    @Override
    public boolean checkCommentExists(ObserveSite observeSite, Long memberId){
        return observeSiteReviewRepository.existsByMemberIdAndObserveSite(memberId, observeSite);
    }

    @Override
    public List<ObserveSiteReview> getReviews(ObserveSite observeSite){
        return observeSiteReviewRepository.findAllByObserveSite(observeSite);
    }
}
