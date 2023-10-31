package com.instargram101.starcard.api;

import com.instargram101.global.common.response.CommonApiResponse;
import com.instargram101.starcard.dto.request.SaveCardRequestDto;
import com.instargram101.starcard.service.StarcardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("")
public class StarcardController {

    private final StarcardService starcardService;

    @GetMapping("/card/{memberId}")
    public ResponseEntity<CommonApiResponse> findCards(@RequestHeader("myId") Long myId, @PathVariable Long memberId){
        return ResponseEntity.ok(CommonApiResponse.OK("조회성공", starcardService.findCards(myId, memberId)));
    }

    @GetMapping("card/search")
    public ResponseEntity<CommonApiResponse> searchCards(@RequestHeader("myId") Long myId, @RequestParam String keyword, @RequestParam String category){
        return ResponseEntity.ok(CommonApiResponse.OK("검색성공", starcardService.searchCards(myId, keyword, category)));
    }

    @GetMapping("card/like/{memberId}")
    public ResponseEntity<CommonApiResponse> findLikeCards(@RequestHeader("myId") Long myId, @PathVariable Long memberId){
        return ResponseEntity.ok(CommonApiResponse.OK("조회성공", starcardService.findLikeCards(myId, memberId)));
    }

    @GetMapping("card/like-member/{cardId}")
    public ResponseEntity<CommonApiResponse> findLikedMembers(@PathVariable Long cardId) {
        return ResponseEntity.ok(CommonApiResponse.OK("조회성공", starcardService.findLikeedMembers(cardId)));
    }

    @DeleteMapping("/card/{cardId}")
    public ResponseEntity<CommonApiResponse> deleteCard(@RequestHeader("myId") Long myId, @PathVariable Long cardId) {
        return ResponseEntity.ok(CommonApiResponse.OK("삭제성공", starcardService.deleteCard(myId, cardId)));
    }

//    @PostMapping(value ="/card", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @PostMapping("/card")
    public ResponseEntity<CommonApiResponse> saveCard(@RequestHeader("myId") Long myId, @RequestPart SaveCardRequestDto requestDto, @RequestPart MultipartFile imageFile) throws IOException {
        return ResponseEntity.ok(CommonApiResponse.OK("카드저장성공", starcardService.saveCard(myId, requestDto, imageFile)));
    }

    @GetMapping("/card/{cardId}/likes")
    public ResponseEntity<CommonApiResponse> likeCard(@RequestHeader("myId") Long myId, @PathVariable Long cardId){
        return ResponseEntity.ok(CommonApiResponse.OK("좋아요작업성공", starcardService.likeCard(myId, cardId)));
    }
}