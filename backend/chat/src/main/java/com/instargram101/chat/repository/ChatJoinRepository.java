package com.instargram101.chat.repository;

import com.instargram101.chat.entity.ChatJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatJoinRepository extends JpaRepository<ChatJoin, Long> {
}
