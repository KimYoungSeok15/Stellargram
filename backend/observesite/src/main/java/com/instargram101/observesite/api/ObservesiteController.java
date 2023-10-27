package com.instargram101.observesite.api;

import com.instargram101.global.common.response.CommonApiResponse;
import com.instargram101.observesite.business.ObservesiteBusiness;
import com.instargram101.observesite.dto.request.ObserveSiteInfoRequest;
import com.instargram101.observesite.dto.response.SampleResponse;
import com.instargram101.observesite.service.ObserveSiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class ObservesiteController {

    private final ObservesiteBusiness observeSiteBusiness;

    @GetMapping("test")
    public ResponseEntity<CommonApiResponse> sampleController(){ //ResponseEntity로 안 감싸줘도 됨.

        var data = SampleResponse.builder()
                .status("OK")
                .build();

        //var response = CommonApiResponse.OK(data); // 기본 message "ok" 출력.
        var response = CommonApiResponse.OK("sample message", data); //넣고 싶은 데이터를 넣으면 된다.
        return ResponseEntity.ok(response);
    }


    //TODO: ObserveSiteInfoRequest request를 받아 관측지 정보와 함께 사용자 정보를 넘겨줄 예정.
    @PostMapping("")
    public ResponseEntity<CommonApiResponse> createObserveSite(@RequestBody ObserveSiteInfoRequest request,
                                                               @RequestHeader("myId") Long memberId){
        return new ResponseEntity(observeSiteBusiness.createObserveSite(request, memberId), HttpStatus.valueOf(201));
    }

    //TODO: Latitude와 Longitude를 Path Variable로 받아 그 근처의 관측지를 반환
    @GetMapping("latitude/{latitude}/longitude/{longitude}")
    public ResponseEntity<CommonApiResponse> searchObserveSite(@PathVariable Float latitude,
                                                               @PathVariable Float longitude){
        return null;
    }

    //TODO: Latitude와 Longitude를 Path Variable로 받아 내용 수정(테스트 용)
    @PatchMapping("latitude/{latitude}/longitude/{longitude}")
    public ResponseEntity<CommonApiResponse> modifyObserveSite(@PathVariable Float latitude,
                                                               @PathVariable Float longitude,
                                                               @RequestBody ObserveSiteInfoRequest request){
        return null;
    }

    //TODO: Latitude와 Longitude를 Path Variable로 받아 삭제(테스트 용)
    @DeleteMapping("latitude/{latitude}/longitude/{longitude}")
    public ResponseEntity<CommonApiResponse> deleteObserveSite(@PathVariable Float latitude,
                                                               @PathVariable Float longitude){
        return null;
    }
}

