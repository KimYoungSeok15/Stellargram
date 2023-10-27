package com.instargram101.observesite.mapper;

import com.instargram101.observesite.dto.request.ObserveSiteInfoRequest;
import com.instargram101.observesite.entity.ObserveSite;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ObserveSiteRequestMapper {

    ObserveSite toEntity(ObserveSiteInfoRequest request);

}
