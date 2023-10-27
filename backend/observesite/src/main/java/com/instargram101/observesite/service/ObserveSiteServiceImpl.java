package com.instargram101.observesite.service;

import com.instargram101.global.common.exception.customException.CustomException;
import com.instargram101.observesite.entity.ObserveSite;
import com.instargram101.observesite.exception.errorCode.ObservesiteErrorCode;
import com.instargram101.observesite.repoository.ObserveSiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ObserveSiteServiceImpl implements ObserveSiteService{


    private final ObserveSiteRepository observeSiteRepository;

    //요청을 쪼개서 새로운 observe site를 생성하는 서비스 로직.
    @Override
    public ObserveSite createNewObserveSite(ObserveSite observeSite, Long memberId){
        //새로운 observe site이니 review count, total rating도 0으로 바꾸기.
        observeSite.setRatingSum(Long.valueOf("0"));
        observeSite.setReviewCount(Long.valueOf("0"));
        observeSite.setMemberId(memberId);
        observeSite.setObserveSiteId(genObserveSiteId(observeSite.getLatitude(), observeSite.getLongitude()));
        return observeSiteRepository.save(observeSite);
    }

    @Override
    public String genObserveSiteId(Float latitude, Float longitude){
        float lati = latitude.floatValue();
        Integer a = Integer.valueOf((int) (lati * 1000.0));
        float longi = longitude.floatValue();
        Integer b = Integer.valueOf((int) (longi * 1000.0));
        return a.toString() + "-" + b.toString();
    }

    @Override
    public ObserveSite getObserveSite(Float latitude, Float longitude){
        String siteId = genObserveSiteId(latitude, longitude);
        return observeSiteRepository.findById(siteId).orElseThrow(() ->
                new CustomException(ObservesiteErrorCode.Observesite_Not_Found,
                "There is no observe site that you requested."));
    }

    //전체 평점, 댓글 수 업데이트
    @Override
    public ObserveSite updateObserveSite(ObserveSite observeSite, Long rating){
        var reviewCount = observeSite.getReviewCount();
        var ratingSum = observeSite.getRatingSum();

        observeSite.setReviewCount(reviewCount + 1);
        observeSite.setRatingSum(ratingSum + rating);

        return observeSiteRepository.save(observeSite);
    }
}
