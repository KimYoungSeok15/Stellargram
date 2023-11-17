package com.instargram101.chat.service;

import com.instargram101.chat.client.MemberFeignClient;
import com.instargram101.chat.config.ChattingConfig;
import com.instargram101.chat.dto.request.MemberListFeignRequest;
import com.instargram101.chat.dto.response.MemberInfo;
import com.instargram101.chat.dto.response.MessageListResponse;
import com.instargram101.chat.dto.response.MessageResponse;
import com.instargram101.chat.entity.AutoSequence;
import com.instargram101.chat.entity.ChatMessage;
import com.instargram101.chat.repository.ChatRoomRepository;
import com.instargram101.chat.repository.CountersRepository;
import com.instargram101.chat.repository.MessageRepository;
import com.instargram101.global.common.response.CommonApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final CountersRepository autoSequenceRepository;
    private final ChattingConfig chattingConfig;
    private final RedisPublisher redisPublisher;

    private final MemberFeignClient memberFeignClient;

    // 특정 채팅방에 메세지 보내기
    @Override
    public boolean sendMessageToRoom(Long roomId, ChatMessage message) {

        // 가장 최근 seq가져오기
        Optional<AutoSequence> searchedSeq = autoSequenceRepository.findById("messageSeq");
        Long recentSeq = null;
        AutoSequence messageSeq = null;

        // seq가 없다면 생성
        if (searchedSeq.isEmpty()) {
            messageSeq = AutoSequence.builder().id("messageSeq").seq(Long.valueOf(0)).build();
        } else messageSeq = searchedSeq.get();

        // seq 증가
        messageSeq.increaseSeq();

        long nextSeq = messageSeq.getSeq();

        // 메세지 저장
        message.setSeq(nextSeq);
        messageRepository.save(message);

        // 증가된 seq 저장
        autoSequenceRepository.save(messageSeq);

        // 멤버 정보 openfeign으로 가져오기
        List<Long> memberIdList = new ArrayList<>();
        memberIdList.add(message.getMemberId());
        ResponseEntity<CommonApiResponse> response = memberFeignClient.getMemberListsById(MemberListFeignRequest.of(memberIdList));

        LinkedHashMap data = (LinkedHashMap) response.getBody().getData();
        List<LinkedHashMap> memberResponse = (ArrayList) data.get("members");
        LinkedHashMap member = memberResponse.get(0);


        // 가져온 정보로 발행할 메세지 형식 맞추기
        MessageResponse newMessage = MessageResponse.of(
                message.getSeq(),
                message.getUnixTimestamp(),
                message.getMemberId(),
                (String) member.get("nickname"),
                (String) member.get("profileImageUrl"),
                message.getContent()
                , message.getRoomId());

        // 메세지 redis에 발행
        redisPublisher.publishMessage(roomId, newMessage);

        return false;
    }

    // 특정 채팅방 가장 마지막 페이지 커서 반환하기
    @Override
    public Long getRecentCursor(Long roomId) {
        // 방 존재 여부 확인
        if (!chatRoomRepository.existsById(roomId)) {
            throw new IllegalArgumentException("채팅방이 존재하지 않아 최근 커서를 조회할 수 없습니다.");
        }

        Long count = messageRepository.countByRoomId(roomId);
        Long cursor = count / chattingConfig.getPageSize();
        if (count % chattingConfig.getPageSize() == 0) cursor--;

        return cursor;
    }


    //이전 채팅 가져오기, 300씩 페이지네이션? 페이지네이션 해 말아?
    @Override
    public MessageListResponse getMessagesOfRoom(Long roomId, int cursor) {

        // 방 존재 여부 확인
        if (!chatRoomRepository.existsById(roomId)) {
            throw new IllegalArgumentException("채팅방이 존재하지 않아 메세지를 조회할 수 없습니다.");
        }

        // 페이저블 객체설정, 다음 커서 설정
        Pageable pageable = PageRequest.of(cursor, chattingConfig.getPageSize());
        int nextCursor = cursor - 1;

        //페이징해서 정보 가져오기
        List<ChatMessage> searchResult = messageRepository.findByRoomIdOrderByUnixTimestampAsc(roomId, pageable).getContent();
//        List<ChatMessage> searchResult = messageRepository.findByRoomId(roomId,pageable).getContent();

//        // 마지막 페이지라면 다음커서 -1
//        if (searchResult.size() < chattingConfig.getPageSize()) nextCursor = -1;

        // 다음 커서는 이전페이지
        nextCursor = cursor - 1;

        // 검색된 메세지의 멤버Id 리스트 만들기
        List<Long> memberIdList = new ArrayList<>();
        for (ChatMessage chat : searchResult) {
            // 증복제거 안함
            memberIdList.add(chat.getMemberId());
            // 중복제거
//            Long thisId = chat.getMemberId();
//            if(!memberIdList.contains(thisId))
//                memberIdList.add(thisId);
        }
        System.out.println(memberIdList);

        // openfiegn으로 가져오기
        ResponseEntity<CommonApiResponse> response = memberFeignClient.getMemberListsById(MemberListFeignRequest.of(memberIdList));

        LinkedHashMap data = (LinkedHashMap) response.getBody().getData();

        List<LinkedHashMap> memberResponse = (ArrayList) data.get("members");

        // 가져온 정보를 memberId를 키로가지는 Map으로 변환
        Map<Long, MemberInfo> memberMap = new HashMap<>();
        for (LinkedHashMap item : memberResponse) {
            Long thisMemberId;
            Object temp = item.get("memberId");
            if(temp instanceof Long)
                thisMemberId=((Long) item.get("memberId")).longValue();
            else thisMemberId = ((Integer) item.get("memberId")).longValue();
            String thisNickname = (String) item.get("nickname");
            String thisProfileImageUrl = (String) item.get("profileImageUrl");
            memberMap.put(thisMemberId,
                    MemberInfo.of(thisMemberId, thisNickname, thisProfileImageUrl));
        }

        // 반환할 dto로 변환 시작
        List<MessageResponse> messageResponseList = new ArrayList<>();

        for (ChatMessage thisChat : searchResult) {
            MemberInfo thisMember = memberMap.get(thisChat.getMemberId());

            messageResponseList.add(
                    MessageResponse.builder()
                            .seq(thisChat.getSeq())
                            .time(thisChat.getUnixTimestamp())
                            .memberId(thisMember.getMemberId())
                            .memberImagePath(thisMember.getProfileImageUrl())
                            .memberNickName(thisMember.getNickname())
                            .content(thisChat.getContent())
                            .roomId(thisChat.getRoomId())
                            .build());
        }

        // 가져온 정보 가공해서 response에 담아 반환하기
        return MessageListResponse.of(nextCursor, messageResponseList);

    }

}
