package com.instargram101.observesite.api;

import com.instargram101.global.common.response.CommonApiResponse;
import com.instargram101.observesite.dto.request.ReviewRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
public class ObservesiteReviewController {

    //TODO: 특정 site의 longitude, latitude를 받아 review 목록 반환
    @GetMapping("/latitude/{latitude}/longitude/{longitude}")
    public ResponseEntity<CommonApiResponse> getReviews(@PathVariable Float latitude,
                                                        @PathVariable Float longitude){
        return null;
    }

    //TODO: 특정 site의 review 등록
    @PostMapping("/latitude/{latitude}/longitude/{longitude}")
    public ResponseEntity<CommonApiResponse> createReviews(@PathVariable Float latitude,
                                                           @PathVariable Float longitude,
                                                           @RequestBody ReviewRequestDto request){
        return null;
    }

    //TODO: 특정 site의 review 수정
    @PutMapping("{reviewId}/latitude/{latitude}/longitude/{longitude}")
    public ResponseEntity<CommonApiResponse> modifyReviews(@PathVariable Long reviewId,
                                                           @PathVariable Float latitude,
                                                           @PathVariable Float longitude,
                                                           @RequestBody ReviewRequestDto request){
        return null;
    }

    //TODO: 특정 site의 review 삭제
    @DeleteMapping("{reviewId}/latitude/{latitude}/longitude/{longitude}")
    public ResponseEntity<CommonApiResponse> deleteReviews(@PathVariable Long reviewId,
                                                           @PathVariable Float latitude,
                                                           @PathVariable Float longitude){
        return null;
    }
}
