package com.instargram101.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindFollowListResponseDto {
    @NotBlank
    private Long memberId;
    @NotBlank
    private String nickname;
    @NotBlank
    private String profileImageUrl;
}
