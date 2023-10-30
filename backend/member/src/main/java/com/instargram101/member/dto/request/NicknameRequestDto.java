package com.instargram101.member.dto.request;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class NicknameRequestDto {
    private String nickname;
}