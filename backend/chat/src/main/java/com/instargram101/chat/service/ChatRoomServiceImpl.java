package com.instargram101.chat.service;

import com.instargram101.chat.dto.response.RoomListResponse;
import com.instargram101.chat.dto.response.RoomProcessResponse;
import com.instargram101.chat.entity.ChatJoin;
import com.instargram101.chat.entity.ChatRoom;
import com.instargram101.chat.repository.ChatJoinRepository;
import com.instargram101.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.instargram101.chat.config.ChattingConfig;

@Transactional
@RequiredArgsConstructor
@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatJoinRepository chatJoinRepository;
    private final ChattingConfig chatRoomConfig;
    private final MessageListenerAdapter listenerAdapter;
    private final TopicService topicService;

    // 특정 관측소 아이디를 가지는 채팅방 존재 여부 확인
    @Override
    public boolean isExistRoomOf(String siteId) {

        return chatRoomRepository.existsByObserveSiteId(siteId);

    }

    // 채팅방 생성
    @Override
    public ChatRoom createRoom(String siteId) {

        // TODO: 오픈페인으로 존재하는 관측소인지 확인 로직 추가할 곳

        // 채팅방 생성 후 정보 저장
        ChatRoom newRoom = ChatRoom.builder()
                .personnel(Long.valueOf(0))
                .observeSiteId(siteId)
                .build();

        return chatRoomRepository.save(newRoom);

    }

    // 채팅방 삭제
    @Override
    public boolean deleteRoom(Long roomId) {
        return false;
    }

    // 채팅방 참여
    @Override
    public ChatJoin joinRoom(ChatRoom thisRoom, Long memberId) {
        if (chatRoomRepository.existsById(thisRoom.getRoomId()))
            new IllegalArgumentException("잘못된 채팅방 id로 인해 참여하지 못했습니다.");

        // 채팅방 참여
        ChatJoin newJoin = ChatJoin.builder()
                .memberId(memberId)
                .room(thisRoom)
                .build();

        // 채팅방 인원수++
        thisRoom.increasePersonnel();
        chatRoomRepository.save(thisRoom);

        return chatJoinRepository.save(newJoin);
    }

    // 장소Id로 채팅방 참여
    public RoomProcessResponse joinRoomBySiteId(String siteId, Long memberId) {

        // 회원이 참여한 채팅방 갯수 검증
        if (chatJoinRepository.countByMemberId(memberId) >= chatRoomConfig.getMaxRoomCount())
            throw new IllegalArgumentException("더이상 새 채팅방에 참여할 수 없습니다");

        // 채팅방 존재 여부 검증
        boolean isExistRoom = chatRoomRepository.existsByObserveSiteId(siteId);

        ChatRoom thisRoom = null;

        // 존재한다면 이미 참여했는지 검증
        if (isExistRoom) {
            thisRoom = chatRoomRepository.findByObserveSiteId(siteId).get();

            // 이미 참여했다면 그대로 반환
            if (chatJoinRepository.existsByRoomAndMemberId(thisRoom, memberId)) {
                // 채팅방 인원수가 0이라면 Redis에 해당 방 토픽으로 리스너 추가
                if (thisRoom.getPersonnel() == 0)
                    topicService.addRoomTopicToRedis(listenerAdapter, thisRoom.getRoomId());
//        listenerContainer.addMessageListener(listenerAdapter, new ChannelTopic("room/" + thisRoom.getRoomId()));

                return RoomProcessResponse.of(thisRoom.getRoomId());

            }
        }
        // 존재하지 않는다면 채팅방 생성
        else {
            thisRoom = createRoom(siteId);
        }

        if (thisRoom == null)
            throw new NullPointerException("채팅방 확인 중 에러 발생");

        // 채팅방 인원수가 0이라면 Redis에 해당 방 토픽으로 리스너 추가
        if (thisRoom.getPersonnel() == 0)
            topicService.addRoomTopicToRedis(listenerAdapter, thisRoom.getRoomId());
//        listenerContainer.addMessageListener(listenerAdapter, new ChannelTopic("room/" + thisRoom.getRoomId()));

        // 채팅방 참여
        joinRoom(thisRoom, memberId);

        return RoomProcessResponse.of(thisRoom.getRoomId());

    }


    // 채팅방 탈퇴
    @Override
    public RoomProcessResponse leaveRoom(Long roomId, Long memberId) {

        // 채팅방 존재 여부 검증
        boolean isExistRoom = chatRoomRepository.existsById(roomId);

        // 존재하지 않는다면 에러
        if (!isExistRoom)
            throw new IllegalArgumentException("존재하지 않는 채팅방이므로 탈퇴할 수 없습니다");

        ChatRoom thisRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new NoSuchElementException("채팅방을 찾을 수 없습니다"));

        // 채팅방 참여 여부 검증
        boolean isExistJoin = chatJoinRepository.existsByRoomAndMemberId(thisRoom, memberId);

        // 참여하지 않았다면 에러
        if (!isExistJoin)
            throw new IllegalArgumentException("참여하지 않은 채팅방이므로 탈퇴할 수 없습니다.");

        // 채팅방 나가기 처리
        chatJoinRepository.deleteByRoomAndMemberId(thisRoom, memberId);

        // 방 인원수 --
        thisRoom.decreasePersonnel();

        // 방 인원수 0이라면 레디스 토픽 제거
        if (thisRoom.getPersonnel() == 0) {
            topicService.removeRoomTopicToRedis(listenerAdapter, roomId);
            // 방 정보 자체는 삭제되지 않는다. 몽고디비에 저장하는 메세지를 roomId로 저장하기 때문
            // TODO: 추후 방정보 삭제가 필요하다면 방 정보에 deleted 추가하거나, 메세지에 roomId 외에도 siteId 를 추가해야 함
        }

        // 방 인원수--된 방 정보 업데이트
        chatRoomRepository.save(thisRoom);

        return RoomProcessResponse.of(roomId);

    }

    // 채팅방 참여 했다면 참여여부 찾아서 반환
    @Override
    public ChatRoom findRoomIfJoined(Long roomId, Long memberId) {
        Optional<ChatRoom> searchedRoom = chatJoinRepository.findByRoomIdAndMemberId(roomId, memberId);
        if (searchedRoom.isPresent())
            return searchedRoom.get();
        else
            return null;
    }

    // 유저가 참여한 채팅방 목록 전체 조회
    @Override
    public RoomListResponse findMyRooms(Long memberId) {
        List<ChatRoom> roomList = chatJoinRepository.findJoinedRoomByMemberId(memberId);

        return RoomListResponse.of(Long.valueOf(roomList.size()), roomList);
    }


}
