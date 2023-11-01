package com.instargram101.chat.config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ChattingConfig {

    // 최대 채팅방 수
    private int maxRoomCount = 5;

    // 채팅방 별 최대 인원 수
    private int maxPersonnelCount = 100;

    // 페이지네이션 최대 갯수
    private int pageSize = 300;
}
