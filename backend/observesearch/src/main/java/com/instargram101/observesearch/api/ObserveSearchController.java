package com.instargram101.observesearch.api;

import com.instargram101.global.common.response.CommonApiResponse;
import com.instargram101.observesearch.converter.ObserveSiteListConverter;
import com.instargram101.observesearch.dto.response.ObserveSiteResponseDto;
import com.instargram101.observesearch.entity.ObserveSite;
import com.instargram101.observesearch.service.ObserveSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class ObserveSearchController {

    private final ObserveSearchService observeSearchService;

    private final ObserveSiteListConverter observeSiteListConverter;

    @GetMapping("")
    public ResponseEntity<CommonApiResponse> getTopObserveSites(@RequestParam("startX") Double startX,
                                                                @RequestParam("endX") Double endX,
                                                                @RequestParam("startY") Double startY,
                                                                @RequestParam("endY") Double endY)
    {
        List<ObserveSite> observeSites = observeSearchService.getTopObserveSites(startX, endX, startY, endY);
        List<ObserveSiteResponseDto> response = observeSiteListConverter.toResponse(observeSites);
        return ResponseEntity.ok(
                CommonApiResponse.OK(response)
        );
    }

    @PutMapping("latitude/{latitude}/longitude/{longitude}")
    public ResponseEntity<CommonApiResponse> updateObserveSite(@PathVariable Double latitude,
                                                               @PathVariable Double longitude)
    {
        ObserveSite observeSite = observeSearchService.updateSingleObserveSite(longitude, latitude);
        Map<String, Object> response = new HashMap<>();

        response.put("status", "OK");
        return ResponseEntity.ok(CommonApiResponse.OK(response));
    }

    @PostMapping("latitude/{latitude}/longitude/{longitude}")
    public ResponseEntity<CommonApiResponse> addObserveSite(@PathVariable Double latitude,
                                                               @PathVariable Double longitude)
    {
        ObserveSite observeSite = observeSearchService.addSingleObserveSite(longitude, latitude);
        Map<String, Object> response = new HashMap<>();

        response.put("status", "OK");
        return ResponseEntity.ok(CommonApiResponse.OK(response));
    }
}
