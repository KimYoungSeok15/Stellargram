package com.instargram101.observesearch.converter.mapper;

import com.instargram101.observesearch.dto.response.ObserveSiteResponseDto;
import com.instargram101.observesearch.entity.ObserveSite;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ObserveSiteResponseMapper {

    ObserveSiteResponseDto toResponse(ObserveSite observeSite);

}
