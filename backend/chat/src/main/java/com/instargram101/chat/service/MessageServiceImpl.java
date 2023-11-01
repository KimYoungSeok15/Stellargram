package com.instargram101.chat.service;

import com.instargram101.chat.config.ChattingConfig;
import com.instargram101.chat.dto.response.MessageListResponse;
import com.instargram101.chat.entity.AutoSequence;
import com.instargram101.chat.entity.ChatMessage;
import com.instargram101.chat.repository.ChatRoomRepository;
import com.instargram101.chat.repository.CountersRepository;
import com.instargram101.chat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final CountersRepository autoSequenceRepository;
    private final ChattingConfig chattingConfig;
    private final RedisPublisher redisPublisher;

    // 특정 채팅방에 메세지 보내기
    @Override
    public boolean sendMessageToRoom(Long roomId, ChatMessage message) {

        // 가장 최근 seq가져오기
        Optional<AutoSequence> searchedSeq =  autoSequenceRepository.findById("messageSeq");
        Long recentSeq = null;
        AutoSequence messageSeq = null;

        // seq가 없다면 생성
        if(searchedSeq.isEmpty()) {
            messageSeq = AutoSequence.builder()
                    .id("messageSeq")
                    .seq(Long.valueOf(0)).build();
        }
        else
            messageSeq = searchedSeq.get();

        // seq 증가
        messageSeq.increaseSeq();

        long nextSeq = messageSeq.getSeq();

        // 메세지 저장
        message.setSeq(nextSeq);
        messageRepository.save(message);

        // 증가된 seq 저장
        autoSequenceRepository.save(messageSeq);

        // 메세지 redis에 발행
        redisPublisher.publishMessage(roomId,message);

        return false;
    }

    //이전 채팅 가져오기, 300씩 페이지네이션? 페이지네이션 해 말아?
    @Override
    public MessageListResponse getMessagesOfRoom(Long roomId, int cursor) {

        // 방 존재 여부 확인
        if( !chatRoomRepository.existsById(roomId)){
            throw new IllegalArgumentException("채팅방이 존재하지 않아 메세지를 조회할 수 없습니다.");
        }

        // 페이저블 객체설정, 다음 커서 설정
        Pageable pageable = PageRequest.of(cursor, chattingConfig.getPageSize());
        int nextCursor = cursor+1;

        //페이징해서 정보 가져오기
        List<ChatMessage> searchResult = messageRepository.findByRoomIdOrderByUnixTimestampDesc(roomId,pageable).getContent();

        // 마지막 페이지라면 다음커서 -1
        if (searchResult.size() < chattingConfig.getPageSize())
            nextCursor = -1;

        // 가져온 정보 가공해서 response에 담아 반환하기
        return MessageListResponse.of(nextCursor,searchResult);

    }

}
