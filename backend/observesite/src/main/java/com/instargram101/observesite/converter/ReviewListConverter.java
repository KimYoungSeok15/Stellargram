package com.instargram101.observesite.converter;

import com.instargram101.global.annotation.Converter;
import com.instargram101.observesite.converter.mapper.ReviewResponseMapper;
import com.instargram101.observesite.dto.response.ReviewInfoResponseDto;
import com.instargram101.observesite.entity.ObserveSiteReview;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Converter
@RequiredArgsConstructor
public class ReviewListConverter {

    private final ReviewResponseMapper reviewResponseMapper;
    public List<ReviewInfoResponseDto> toResponse(List<ObserveSiteReview> reviews){
        List<ReviewInfoResponseDto> responses = new ArrayList<>();
        for(ObserveSiteReview review : reviews){
            responses.add(reviewResponseMapper.toResponse(review));
        }
        return responses;
    }
}
