package com.instargram101.chat.repository;

import com.instargram101.chat.entity.ChatJoin;
import com.instargram101.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatJoinRepository extends JpaRepository<ChatJoin, Long> {
    // 특정 유저가 참여한 채팅방 목록 조회
    @Query("SELECT j.room FROM ChatJoin AS j WHERE j.memberId = :memberId")
    List<ChatRoom> findJoinedRoomByMemberId(Long memberId);

    // 특정 유저가 참여한 채팅방 갯수 조회
    Long countByMemberId(Long memberId);

    // 채팅방과 유저의 id에 맞는 채팅참여 존재 확인
    Boolean existsByRoomAndMemberId(ChatRoom room,Long memberId);

    // 채팅방과 유저의 id에 맞는 채팅참여 삭제
    void deleteByRoomAndMemberId(ChatRoom room, Long memberId);

    // 채팅방id와 유저id로 일치하는 참여정보 조회 후 채팅방 객체 반환
    @Query("SELECT j.room FROM ChatJoin AS j WHERE j.memberId= :memberId AND j.room.roomId = :roomId")
    Optional<ChatRoom> findByRoomIdAndMemberId(Long roomId,Long memberId);

    List<ChatJoin> findAllByMemberId(Long memberId);
}