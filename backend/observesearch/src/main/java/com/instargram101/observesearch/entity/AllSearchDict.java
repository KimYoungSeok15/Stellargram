package com.instargram101.observesearch.entity;

import com.instargram101.observesearch.repository.ObserveSearchRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public class AllSearchDict {

    private final ObserveSiteSorter observeSiteSorter;

    private final ObserveSearchRepository observeSearchRepository;

    private HashMap<Long, List<ObserveSite>> dict = new HashMap<>();

    public long getIdByLongLatiChunk(long longChunk, long latiChunk){
        long bigNumber = 1000000000L;
        return bigNumber * longChunk + latiChunk;
    }

    /**
    * <p> ObserveSite를 추가하는 함수 </p>
    * @param longChunk 경도 청크의 index
    * @param latiChunk 위도 청크의 index
     * @param observeSite 해당 관측지
     *
     * @return 아무것도 반환하지 않음.
    */
    public void addObserveSite(long longChunk, long latiChunk, ObserveSite observeSite){
        // 2차원 해시맵을 사용하지 않고 chunk의 최대 개수보다 큰 bigNumber를 이용하여 id를 만들어줌
        long id = Long.valueOf(getIdByLongLatiChunk(longChunk, latiChunk));

        //우선 해당 id가 있으면 그 안에 넣고, 아님 해당 id에 새로운 List를 만들어준 뒤 그 안에 observeSite를 집어넣기
        if(!dict.containsKey(id))
            dict.put(id, new ArrayList<>());
        dict.get(id).add(observeSite);
    }

    /**
     * 특정 id에 저장되어 있는 observeSite들을 별점 순으로 정렬하기
     * @param longChunk 경도 청크 id
     * @param latiChunk 위도 청크 id
     *
     * @return 아무것도 반환하지 않음
     */
    public void sortSitesById(long longChunk, long latiChunk){
        long id = Long.valueOf(getIdByLongLatiChunk(longChunk, latiChunk));
        List<ObserveSite> sites = dict.get(id);
        sites.sort(observeSiteSorter);
    }

    /**
     * 청크별로 모든 observeSite들을 별점 순으로 정렬.
     *
     * @return 아무것도 반환하지 않음
     */
    public void sortAllSites(){
        for(Long id: dict.keySet()){
            List<ObserveSite> sites = dict.get(id);
            sites.sort(observeSiteSorter);
        }
    }

    /**
     * dict 해시맵에 있는 원소들을 보여주는 함수
    * */
    @Override
    public String toString(){
        List<String> res = new ArrayList<>();
        for(Long id : dict.keySet()){
            long idValue = id.longValue();
            long longChunk = idValue / 1000000000L;
            long latiChunk = idValue % 1000000000L;
            res.add(String.format("long: %d lati: %d", longChunk, latiChunk));

            List<ObserveSite> sites = dict.get(id);
            for(ObserveSite site: sites){
                res.add(site.toString());
            }
        }
        return String.join("\n", res);
    }

}
