package com.instargram101.observesite.business;

import com.instargram101.global.annotation.Business;
import com.instargram101.global.common.response.CommonApiResponse;
import com.instargram101.observesite.dto.request.ObserveSiteInfoRequestDto;
import com.instargram101.observesite.dto.response.ObserveSiteResponseDto;
import com.instargram101.observesite.converter.mapper.ObserveSiteRequestMapper;
import com.instargram101.observesite.converter.mapper.ObserveSiteResponseMapper;
import com.instargram101.observesite.service.ObserveSiteServiceImpl;
import lombok.RequiredArgsConstructor;

@Business
@RequiredArgsConstructor
public class ObservesiteBusiness {

    private final ObserveSiteRequestMapper observeSiteRequestMapper;

    private final ObserveSiteResponseMapper observeSiteResponseMapper;

    private final ObserveSiteServiceImpl observeSiteService;

    public CommonApiResponse<ObserveSiteResponseDto> createObserveSite(ObserveSiteInfoRequestDto request, Long memberId){
        var observeSite = observeSiteRequestMapper.toEntity(request);
        var newObserveSite = observeSiteService.createNewObserveSite(observeSite, memberId);
        var response = observeSiteResponseMapper.toResponse(newObserveSite);
        return CommonApiResponse.WELL_CREATED(response);
    }

    public CommonApiResponse<ObserveSiteResponseDto> getObserveSite(Float latitude, Float longitude){
        var observeSite = observeSiteService.getObserveSite(latitude, longitude);
        var response = observeSiteResponseMapper.toResponse(observeSite);
        return CommonApiResponse.OK(response);
    }
}
