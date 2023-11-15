package com.instargram101.observesearch.service;

import com.instargram101.observesearch.entity.AllSearchDict;
import com.instargram101.observesearch.entity.ObserveSite;
import com.instargram101.observesearch.repository.ObserveSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ObserveSearchService {
    private final ObserveSearchRepository observeSearchRepository;

    private final AllSearchDict allSearchDict;

    @Value("${value.chunk_size}")
    private long chunkSize;

    @Value("${value.maximum_rendering_observe_site}")
    private int maximumObserveSite;

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

    //처음에 observeSite가 추가될 경우.
    public ObserveSite addSingleObserveSite(Double longitude, Double latitude){
        String id = genObserveSiteId(latitude, longitude);
        ObserveSite observeSite = observeSearchRepository.findById(id).orElseThrow();

        long longChunk = getChunkId(longitude);
        long latiChunk = getChunkId(latitude);

        allSearchDict.addObserveSite(longChunk, latiChunk, observeSite);
        allSearchDict.sortSitesById(longChunk, latiChunk);
        return observeSite;
    }


    //댓글이 달릴 때마다 observaSite 정보가 갱신됨.
    public ObserveSite updateSingleObserveSite(Double longitude, Double latitude){
        String id = genObserveSiteId(latitude, longitude);
        ObserveSite observeSite = observeSearchRepository.findById(id).orElseThrow();

        long longChunk = getChunkId(longitude);
        long latiChunk = getChunkId(latitude);

        allSearchDict.updateObserveSite(longChunk, latiChunk, observeSite);
        allSearchDict.sortSitesById(longChunk, latiChunk);
        return observeSite;
    }

    //해당 범위의 모든 청크 조사
    /**
     *
     * @param startX 시작 경도
     * @param endX 끝 경도
     * @param startY 시작 위도
     * @param endY  끝 위도
     *
     * @return 가능한 청크의 모든 키 값 반환
    * */
    public List<Long> getAllChunkFromRange(Double startX, Double endX, Double startY, Double endY){
        List<Long> chunkList = new ArrayList<>();

        long startXChunk = getChunkId(startX);
        long endXChunk = getChunkId(endX);
        long startYChunk = getChunkId(startY);
        long endYChunk = getChunkId(endY);

        for(long longi = startXChunk; longi <= endXChunk; longi++){
            for(long lati = startYChunk; lati <= endYChunk; lati++){
                long id = allSearchDict.getIdByLongLatiChunk(longi, lati);
                if(allSearchDict.getSize(id) == 0) continue;
                chunkList.add(id);
            }
        }
        return chunkList;
    }

    public List<ObserveSite> getTopObserveSites(Double startX, Double endX, Double startY, Double endY){
//        long totalCount = 0L;
//
//        List<Long> chunkList = getAllChunkFromRange(startX, endX, startY, endY);
//        List<Integer> orders = new ArrayList(Collections.nCopies(chunkList.size(), 0));
//
//        for(Long chunk: chunkList){
//            totalCount += allSearchDict.getSize(chunk);
//        }
//
//        List<ObserveSite> observeSites = new ArrayList<>();
//        int i = 0;
//        int j = 0;
//        for(; i < maximumObserveSite && j < totalCount; j++){
//            int id = allSearchDict.getTopObserveSite(chunkList, orders);
//            ObserveSite observeSite = allSearchDict.getObserveSite(chunkList.get(id), orders.get(id));
//            if(observeSite.getLatitude() >= startX &&
//                    observeSite.getLatitude() <= endX &&
//                    observeSite.getLongitude() >= startY &&
//                    observeSite.getLongitude() <= endY
//            ){
//                observeSites.add(allSearchDict.getObserveSite(chunkList.get(id), orders.get(id)));
//                int order = orders.get(id);
//                orders.set(id, order + 1);
//                i++;
//            }
//        }
        return allSearchDict.getTopObserveSite(startX, endX, startY, endY);
    }
}
