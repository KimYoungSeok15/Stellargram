package com.instargram101.observesite.converter.mapper;

import com.instargram101.observesite.dto.response.ReviewInfoResponseDto;
import com.instargram101.observesite.entity.ObserveSiteReview;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewResponseMapper {
    ReviewInfoResponseDto toResponse(ObserveSiteReview review);
}
