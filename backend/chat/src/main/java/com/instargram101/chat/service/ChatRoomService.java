package com.instargram101.chat.service;

import com.instargram101.chat.dto.response.RoomListResponse;
import com.instargram101.chat.dto.response.RoomProcessResponse;
import com.instargram101.chat.entity.ChatJoin;
import com.instargram101.chat.entity.ChatRoom;

public interface ChatRoomService {

    // 특정 관측소 아이디를 가지는 채팅방 존재 여부 확인
    public boolean isExistRoomOf(String siteId);

    // 채팅방 생성
    public ChatRoom createRoom(String siteId);


    // 채팅방 삭제
    public boolean deleteRoom(Long roomId);


//    // 채팅방 참여
    public ChatJoin joinRoom(ChatRoom thisRoom, Long memberId);

    // 장소Id로 채팅방 참여
    public RoomProcessResponse joinRoomBySiteId(String siteId, Long memberId);

    // 채팅방 탈퇴
    public RoomProcessResponse leaveRoom(Long roomId, Long memberId);

    // 채팅방 참여 했다면 찾아서 반환
    public ChatRoom findRoomIfJoined(Long roomId, Long memberId);

    // 유저가 참여한 채팅방 목록 전체 조회
    public RoomListResponse findMyRooms(Long memberId);

}
