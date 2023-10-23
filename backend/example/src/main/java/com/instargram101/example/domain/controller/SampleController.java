package com.instargram101.example.domain.controller;

import com.instargram101.example.domain.entity.Sample;
import com.instargram101.example.domain.service.SampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sample")
public class SampleController {

    private final SampleService sampleService;
    
    @PostMapping("")
    public ResponseEntity<?> test(@RequestBody Map<String, Object> request){
        Sample sample = new Sample();
        sample.setName((String) request.get("name"));
        Sample newSample = sampleService.save(sample);
        return ResponseEntity.ok(newSample);
    }
    
    @GetMapping("name/{name}")
    public ResponseEntity<?> getSampleByName(@PathVariable String name){
        return ResponseEntity.ok(sampleService.getSample(name));
    }
    
    @GetMapping("id/{id}")
    public ResponseEntity<?> getSampleById(@PathVariable Long id){
        return ResponseEntity.ok(sampleService.getSample(id));
    }
}
