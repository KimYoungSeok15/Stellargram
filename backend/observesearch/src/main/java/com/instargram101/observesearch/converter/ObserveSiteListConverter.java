package com.instargram101.observesearch.converter;

import com.instargram101.global.annotation.Converter;
import com.instargram101.observesearch.converter.mapper.ObserveSiteResponseMapper;
import com.instargram101.observesearch.dto.response.ObserveSiteResponseDto;
import com.instargram101.observesearch.entity.ObserveSite;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Converter
@RequiredArgsConstructor
public class ObserveSiteListConverter {

    private final ObserveSiteResponseMapper observeSiteResponseMapper;
    public List<ObserveSiteResponseDto> toResponse(List<ObserveSite> observeSiteList){
        List<ObserveSiteResponseDto> response = new ArrayList<>();
        for(ObserveSite observeSite : observeSiteList){
            response.add(observeSiteResponseMapper.toResponse(observeSite));
        }
        return response;
    }
}
