package com.instargram101.observesite.service;

import com.instargram101.observesite.entity.ObserveSite;

public interface ObserveSiteService {
    //요청을 쪼개서 새로운 observe site를 생성하는 서비스 로직.
    ObserveSite createNewObserveSite(ObserveSite observeSite, Long memberId);

    String genObserveSiteId(Float latitude, Float longitude);

    ObserveSite getObserveSite(Float latitude, Float longitude);

    //전체 평점, 댓글 수 업데이트
    ObserveSite updateObserveSite(ObserveSite observeSite, Long rating);
}
