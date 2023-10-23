package com.instargram101.example.domain.service;

import com.instargram101.example.domain.entity.Sample;
import com.instargram101.example.domain.repository.SampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SampleService implements SampleServiceImpl{

    private final SampleRepository sampleRepository;

    @Override
    public Sample save(Sample sample){
        return sampleRepository.save(sample);
    }

    @Override
    public List<Sample> getSample(String name){
        return sampleRepository.findAllByName(name);
    }

    @Override
    public Sample getSample(Long id){
        return sampleRepository.findById(id).orElseThrow(() -> new RuntimeException("No such sample"));
    }
}
