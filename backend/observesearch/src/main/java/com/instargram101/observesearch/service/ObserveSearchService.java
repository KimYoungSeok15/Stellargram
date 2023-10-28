package com.instargram101.observesearch.service;

import com.instargram101.observesearch.entity.AllSearchDict;
import com.instargram101.observesearch.entity.ObserveSite;
import com.instargram101.observesearch.repository.ObserveSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ObserveSearchService {
    private final ObserveSearchRepository observeSearchRepository;

    private final AllSearchDict allSearchDict;

    @Value("${value.chunk_size}")
    private long chunkSize;

    public long getChunkId(Double angle){
        return (long) (angle.doubleValue() * 1000.0) / chunkSize;
    }

    public String genObserveSiteId(Double latitude, Double longitude){
        double lati = latitude;
        Integer a = Integer.valueOf((int) (lati * 1000.0));
        double longi = longitude;
        Integer b = Integer.valueOf((int) (longi * 1000.0));
        return a.toString() + "-" + b.toString();
    }
    @PostConstruct
    public void init(){
        //먼저 데이터 베이스에 있는 모든 함수들을 꺼내기
        List<ObserveSite> observeSites = observeSearchRepository.findAll();

        for(ObserveSite observeSite: observeSites){
            long longChunk = getChunkId(observeSite.getLongitude()); //경도 청크
            long latiChunk = getChunkId(observeSite.getLatitude()); //위도 청크

            allSearchDict.addObserveSite(longChunk, latiChunk, observeSite);
        }
        allSearchDict.sortAllSites();
    }

    public ObserveSite addSingleObserveSite(Double longitude, Double latitude){
        String id = genObserveSiteId(latitude, longitude);
        ObserveSite observeSite = observeSearchRepository.findById(id).orElseThrow();

        long longChunk = getChunkId(longitude);
        long latiChunk = getChunkId(latitude);

        allSearchDict.addObserveSite(longChunk, latiChunk, observeSite);
        allSearchDict.sortSitesById(longChunk, latiChunk);
        return observeSite;
    }



}
