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
public class FindMemberResponseDto {
    @NotBlank
    private Long memberId;
    @NotBlank
    private String nickname;
    @NotBlank
    private String profileImageUrl;
    @NotBlank
    private Boolean isFollow;
    @NotBlank
    private Long followCount;
    @NotBlank
    private Long followingCount;
    @NotBlank
    private Long cardCount;
}
