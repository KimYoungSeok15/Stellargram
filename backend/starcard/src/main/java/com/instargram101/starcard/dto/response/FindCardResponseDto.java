package com.instargram101.starcard.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindCardResponseDto {

    private StarcardElement starcard;

    public static FindCardResponseDto of(StarcardElement starcardElement) {
        return FindCardResponseDto.builder()
                .starcard(starcardElement)
                .build();
    }
}