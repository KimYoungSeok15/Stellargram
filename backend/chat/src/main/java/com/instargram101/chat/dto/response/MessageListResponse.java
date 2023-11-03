package com.instargram101.chat.dto.response;

import com.instargram101.chat.entity.ChatMessage;
import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageListResponse {
    int nextCursor;

    List<ChatMessage> messageList;

    public static MessageListResponse of(int nextCursor, List<ChatMessage> messageList){
        return MessageListResponse.builder()
                .nextCursor(nextCursor)
                .messageList(messageList)
                .build();
    }


}
