package com.instargram101.chat.dto.response;

import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageResponse {

    Long seq;

    Long time;

    Long memberId;

    String memberNickName;

    String memberImagePath;

    String content;

    Long roomId;


    public static MessageResponse of(Long seq, Long time, Long memberId, String memberNickName, String memberImagePath, String content,Long roomId) {
        return MessageResponse.builder()
                .seq(seq)
                .time(time)
                .memberId(memberId)
                .memberImagePath(memberImagePath)
                .memberNickName(memberNickName)
                .content(content)
                .roomId(roomId)
                .build();
    }
}
