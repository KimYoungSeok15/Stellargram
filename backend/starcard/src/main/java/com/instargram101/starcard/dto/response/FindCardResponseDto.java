package com.instargram101.starcard.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindCardResponseDto {

    private FindCardResponseElement starcard;

    public static FindCardResponseDto of(FindCardResponseElement starcard) {
        return FindCardResponseDto.builder()
                .starcard(starcard)
                .build();
    }
}