package com.instargram101.starcard.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FindCardsResponseDto {

    private List<StarcardElement> starcards;

    public static FindCardsResponseDto of(List<StarcardElement> starcards) {

        return FindCardsResponseDto.builder()
                .starcards(starcards)
                .build();
    }
}