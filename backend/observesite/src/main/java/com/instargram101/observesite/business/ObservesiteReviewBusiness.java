package com.instargram101.observesite.business;

import com.instargram101.global.annotation.Business;
import com.instargram101.global.common.exception.customException.CustomException;
import com.instargram101.global.common.response.CommonApiResponse;
import com.instargram101.observesite.dto.request.ReviewRequestDto;
import com.instargram101.observesite.dto.response.ReviewInfoResponseDto;
import com.instargram101.observesite.exception.errorCode.ObservesiteErrorCode;
import com.instargram101.observesite.mapper.ReviewRequestMapper;
import com.instargram101.observesite.mapper.ReviewResponseMapper;
import com.instargram101.observesite.service.ObserveSiteReviewServiceImpl;
import com.instargram101.observesite.service.ObserveSiteServiceImpl;
import lombok.RequiredArgsConstructor;

@Business
@RequiredArgsConstructor
public class ObservesiteReviewBusiness {

    private final ReviewRequestMapper reviewRequestMapper;

    private final ReviewResponseMapper reviewResponseMapper;

    private final ObserveSiteServiceImpl observeSiteService;

    private final ObserveSiteReviewServiceImpl observeSiteReviewService;

    public CommonApiResponse<ReviewInfoResponseDto> createReviews(Float latitude, Float longitude, ReviewRequestDto request, Long memberId) {
        var review = reviewRequestMapper.toEntity(request); // 1. 요청을 엔티티로 변환
        var observeSite = observeSiteService.getObserveSite(latitude, longitude); // 2. 해당 위도, 경도의 관측지 찾기
        if(observeSiteReviewService.checkCommentExists(observeSite, memberId)) // 3. 글쓴이가 적은 적이 있으면 에러 반환.
            throw new CustomException(ObservesiteErrorCode.Already_Write_Comment);
        var newReview = observeSiteReviewService.createReview(review, observeSite, memberId); // 4. 리뷰 등록
        observeSiteService.updateObserveSite(observeSite, newReview.getRating()); // 5. 전체 평점 업데이트
        var response = reviewResponseMapper.toResponse(newReview); // 6. 응답 생성
        return CommonApiResponse.WELL_CREATED(response);
    }
}
