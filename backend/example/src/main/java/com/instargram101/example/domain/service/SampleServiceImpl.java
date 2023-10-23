package com.instargram101.example.domain.service;


import com.instargram101.example.domain.entity.Sample;

import java.util.List;

public interface SampleServiceImpl {

    Sample save(Sample sample);

    List<Sample> getSample(String name);

    Sample getSample(Long id);
}
