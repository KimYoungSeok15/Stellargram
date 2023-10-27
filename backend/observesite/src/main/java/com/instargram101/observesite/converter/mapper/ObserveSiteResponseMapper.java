package com.instargram101.observesite.converter.mapper;

import com.instargram101.observesite.dto.response.ObserveSiteResponseDto;
import com.instargram101.observesite.entity.ObserveSite;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ObserveSiteResponseMapper {

    ObserveSiteResponseDto toResponse(ObserveSite observeSite);

}
