package com.instargram101.chat.repository;

import com.instargram101.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {


    // 특정 관측소 아이디에 해당하는 채팅방 조회
    Optional<ChatRoom> findByObserveSiteId(String observeSiteId);

    // 특정 관측소 아이디에 해당하는 채팅방 존재 확인
    boolean existsByObserveSiteId(String observeSiteId);
    
    // 채팅방id에 해당하는 채팅방 존재 확인
    boolean existsByRoomId(Long roomId);

    // 비어있지 않은 채팅방 목록 조회
    @Query("SELECT cr FROM ChatRoom AS cr WHERE cr.personnel > 0" )
    List<ChatRoom> findChatRoomNotEmpty();
}
