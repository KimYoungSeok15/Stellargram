package com.instargram101.observesite.service;

import com.instargram101.global.common.response.CommonApiResponse;
import com.instargram101.observesite.dto.request.ObserveSiteInfoRequest;
import com.instargram101.observesite.dto.response.ObserveSiteResponse;
import com.instargram101.observesite.entity.ObserveSite;
import com.instargram101.observesite.mapper.ObserveSiteRequestMapper;
import com.instargram101.observesite.mapper.ObserveSiteResponseMapper;
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
    public ObserveSite createNewObserveSite(ObserveSite observeSite, Long memberId){
        //새로운 observe site이니 review count, total rating도 0으로 바꾸기.
        observeSite.setRatingSum(Long.valueOf("0"));
        observeSite.setReviewCount(Long.valueOf("0"));
        observeSite.setMemberId(memberId);
        observeSite.setObserveSiteId(genObserveSiteId(observeSite.getLatitude(), observeSite.getLongitude()));
        return observeSiteRepository.save(observeSite);
    }

    public String genObserveSiteId(Float latitude, Float longitude){
        float lati = latitude.floatValue();
        Integer a = Integer.valueOf((int) lati * 1000);
        float longi = longitude.floatValue();
        Integer b = Integer.valueOf((int) longi * 1000);
        return a.toString() + "-" + b.toString();
    }
}
