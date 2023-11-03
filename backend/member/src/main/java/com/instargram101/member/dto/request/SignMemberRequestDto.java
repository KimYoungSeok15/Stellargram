package com.instargram101.member.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class SignMemberRequestDto {

    @NotNull
    private String nickname;
    @NotNull
    private String profileImageUrl;


}
