package com.instargram101.observesite.business;

import com.instargram101.global.annotation.Business;
import com.instargram101.global.common.exception.customException.CustomException;
import com.instargram101.global.common.exception.errorCode.ErrorCode;
import com.instargram101.global.common.response.CommonApiResponse;
import com.instargram101.observesite.dto.request.ObserveSiteInfoRequest;
import com.instargram101.observesite.dto.response.ObserveSiteResponse;
import com.instargram101.observesite.mapper.ObserveSiteRequestMapper;
import com.instargram101.observesite.mapper.ObserveSiteResponseMapper;
import com.instargram101.observesite.service.ObserveSiteServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@Business
@RequiredArgsConstructor
public class ObservesiteBusiness {

    private final ObserveSiteRequestMapper observeSiteRequestMapper;

    private final ObserveSiteResponseMapper observeSiteResponseMapper;

    private final ObserveSiteServiceImpl observeSiteService;

    public CommonApiResponse<ObserveSiteResponse> createObserveSite(ObserveSiteInfoRequest request, Long memberId){
        var observeSite = observeSiteRequestMapper.toEntity(request);
        var newObserveSite = observeSiteService.createNewObserveSite(observeSite, memberId);
        var response = observeSiteResponseMapper.toResponse(newObserveSite);
        return CommonApiResponse.WELL_CREATED(response);
    }
}
