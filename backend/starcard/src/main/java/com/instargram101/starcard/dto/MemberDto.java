package com.instargram101.starcard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class MemberDto {

    private Long memberId;
    private String profileImageurl;
    private String nickname;
}