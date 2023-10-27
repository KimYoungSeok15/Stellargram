package com.instargram101.observesite.converter.mapper;

import com.instargram101.observesite.dto.request.ObserveSiteInfoRequestDto;
import com.instargram101.observesite.entity.ObserveSite;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ObserveSiteRequestMapper {

    ObserveSite toEntity(ObserveSiteInfoRequestDto request);

}
