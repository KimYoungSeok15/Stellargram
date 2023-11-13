package com.instargram101.chat.api;

import com.instargram101.chat.dto.response.SampleResponse;
import com.instargram101.chat.service.ChatRoomService;
import com.instargram101.chat.service.MessageService;
import com.instargram101.global.common.response.CommonApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class ChatController {
    public final ChatRoomService chatRoomService;
    public final MessageService messageService;

    @GetMapping("/test")
    public ResponseEntity<CommonApiResponse> sampleController() { //ResponseEntity로 안 감싸줘도 됨.

        var data = SampleResponse.builder()
                .status("OK")
                .build();

        //var response = CommonApiResponse.OK(data); // 기본 message "ok" 출력.
        var response = CommonApiResponse.OK("sample message", data); //넣고 싶은 데이터를 넣으면 된다.
        return ResponseEntity.ok(response);
    }

    // 내 채팅방 목록 조회
    @GetMapping("/rooms")
    public ResponseEntity<CommonApiResponse> getMyRoomList(@RequestHeader("myId") Long myId) {

        return ResponseEntity.ok(CommonApiResponse.OK("조회 성공", chatRoomService.findMyRooms(myId)));
    }

    // TODO: api명세서에 추가하기
    // 특정 채팅방의 마지막 페이지 커서 조회하기
    @GetMapping("/recentCurser/{chatRoomId}")
    public ResponseEntity<CommonApiResponse> findRecentCursor(@PathVariable(name="chatRoomId") Long chatRoomId){
        return ResponseEntity.ok(CommonApiResponse.OK("조회성공 방 번호:" + chatRoomId, messageService.getRecentCursor(chatRoomId)));
    }

    // 특정 채팅방의 이전 메세지 가져오기
    @GetMapping("open/{chatRoomId}/{cursor}")
    public ResponseEntity<CommonApiResponse> findPastMessages(@PathVariable(name = "chatRoomId") Long chatRoomId, @PathVariable(name = "cursor") int cursor) {
        return ResponseEntity.ok(CommonApiResponse.OK("조회성공 방 번호:" + chatRoomId + " 페이지:" + cursor, messageService.getMessagesOfRoom(chatRoomId, cursor)));
    }

    // 채팅방 참여하기
    @PostMapping("/join/{observeSiteId}")
    public ResponseEntity<CommonApiResponse> joinRoom(@RequestHeader("myId") Long myId, @PathVariable(name = "observeSiteId") String siteId) {
        var response = chatRoomService.joinRoomBySiteId(siteId, myId);
        return ResponseEntity.ok(CommonApiResponse.OK("채팅방 참여 완료. 웹소켓 구독 필요", response));

    }

    // 채팅방 나가기
    @DeleteMapping("/leave/{chatRoomId}")
    public ResponseEntity<CommonApiResponse> leaveRoom(@RequestHeader("myId") Long myId, @PathVariable(name = "chatRoomId") Long chatRoomId) {
        var response = chatRoomService.leaveRoom(chatRoomId, myId);
        return ResponseEntity.ok(CommonApiResponse.OK("채팅방 퇴장 완료. 웹소켓 구독 해체 필요", response));
    }

}
