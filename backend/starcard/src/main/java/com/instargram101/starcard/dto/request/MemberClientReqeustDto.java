package com.instargram101.starcard.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MemberClientReqeustDto {

    List<Long> memberIds;

    public static MemberClientReqeustDto of(List<Long> myIds) {

        return MemberClientReqeustDto.builder()
                .memberIds(myIds)
                .build();
    }
}
