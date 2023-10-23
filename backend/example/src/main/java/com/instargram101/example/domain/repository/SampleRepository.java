package com.instargram101.example.domain.repository;

import com.instargram101.example.domain.entity.Sample;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SampleRepository extends JpaRepository<Sample, Long> {
    List<Sample> findAllByName(String name);
}
