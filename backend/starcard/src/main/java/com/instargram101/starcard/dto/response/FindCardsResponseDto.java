package com.instargram101.starcard.dto.response;

import com.instargram101.starcard.dto.query.StarcardWithAmILikeQueryDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FindCardsResponseDto {

    private List<FindCardResponseElement> starcards;

    public static FindCardsResponseDto of(List<FindCardResponseElement> starcards) {

        return FindCardsResponseDto.builder()
                .starcards(starcards)
                .build();
    }
}