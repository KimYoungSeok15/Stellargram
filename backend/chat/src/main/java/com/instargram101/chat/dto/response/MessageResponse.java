package com.instargram101.chat.dto.response;

import com.instargram101.chat.entity.ChatRoom;
import lombok.*;

import javax.persistence.Id;
import java.util.List;

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

    public static MessageResponse of(Long seq, Long time, Long memberId, String memberNickName, String memberImagePath, String content) {
        return MessageResponse.builder()
                .seq(seq)
                .time(time)
                .memberId(memberId)
                .memberImagePath(memberImagePath)
                .memberNickName(memberNickName)
                .build();
    }
}
