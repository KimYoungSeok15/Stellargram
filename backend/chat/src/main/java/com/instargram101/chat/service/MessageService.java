package com.instargram101.chat.service;

import com.instargram101.chat.dto.response.MessageListResponse;
import com.instargram101.chat.entity.ChatMessage;

public interface MessageService {

    // 특정 채팅방에 메세지 보내기
    public boolean sendMessageToRoom(Long roomId, ChatMessage message);

    // 특정 채팅방의 가장 최근 커서 가져오기
    public Long getRecentCursor(Long roomId);

    // 이전 채팅 가져오기, 300씩 페이지네이션? 페이지네이션 해 말아?
    public MessageListResponse getMessagesOfRoom(Long roomId, int cursor);


}
