package com.instargram101.chat.config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ChattingConfig {

    // 최대 채팅방 수
    private final int maxRoomCount = 100;

    // 채팅방 별 최대 인원 수
    private final int maxPersonnelCount = 100;

    // 페이지네이션 최대 갯수
    private final int pageSize = 10;
}
