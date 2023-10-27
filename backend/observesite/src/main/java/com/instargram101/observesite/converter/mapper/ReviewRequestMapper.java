package com.instargram101.observesite.converter.mapper;

import com.instargram101.observesite.dto.request.ReviewRequestDto;
import com.instargram101.observesite.entity.ObserveSiteReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewRequestMapper {

    ObserveSiteReview toEntity(ReviewRequestDto request);
}
