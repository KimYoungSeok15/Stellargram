package com.instargram101.chat.dto.response;

import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomProcessResponse {
    Long roomNumber;

    public static RoomProcessResponse of(Long roomId){
        return RoomProcessResponse.builder()
                .roomNumber(roomId)
                .build();
    }
}
