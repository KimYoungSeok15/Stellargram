package com.instargram101.chat.dto.response;

import com.instargram101.chat.entity.ChatRoom;
import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomListResponse {

    Long roomCount;

    List<ChatRoom> roomList;

    public static RoomListResponse of(Long roomCount, List<ChatRoom> roomList){
        return RoomListResponse.builder()
                .roomCount(roomCount)
                .roomList(roomList)
                .build();
    }

}
