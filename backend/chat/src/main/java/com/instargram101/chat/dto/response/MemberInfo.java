package com.instargram101.chat.dto.response;

import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfo {
    Long memberId;

    String nickname;

    String profileImageUrl;

    public static MemberInfo of(Long memberId, String nickname, String profileImageUrl) {
        return MemberInfo.builder()
                .memberId(memberId)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
