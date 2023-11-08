package com.instargram101.chat.dto.response;

import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberFeignDto {
    String nickname;
    String profileImageUrl;
    Long followCount;
    Long followingCount;
    Long cardCount;
}
