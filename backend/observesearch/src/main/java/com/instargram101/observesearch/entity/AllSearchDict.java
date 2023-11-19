package com.instargram101.observesearch.entity;

import com.instargram101.global.common.exception.customException.CustomException;
import com.instargram101.global.common.exception.errorCode.ErrorCode;
import com.instargram101.observesearch.repository.ObserveSearchRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.lang.Math.abs;

@Component
@Getter
@Setter
@RequiredArgsConstructor
@Slf4j
public class AllSearchDict {

    private final ObserveSiteSorter observeSiteSorter;

    private final ObserveSearchRepository observeSearchRepository;

    private List<ObserveSite> dict2 = new ArrayList<>();

    private HashMap<Long, List<ObserveSite>> dict = new HashMap<>();

    @Value("${value.maximum_rendering_observe_site}")
    private int maximumObserveSite;

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
//        // 2차원 해시맵을 사용하지 않고 chunk의 최대 개수보다 큰 bigNumber를 이용하여 id를 만들어줌
//        long id = getIdByLongLatiChunk(latiChunk, longChunk);
//
//        //우선 해당 id가 있으면 그 안에 넣고, 아님 해당 id에 새로운 List를 만들어준 뒤 그 안에 observeSite를 집어넣기
//        if(!dict.containsKey(id))
//            dict.put(id, new ArrayList<>());
//        dict.get(id).add(observeSite);
//        log.info(String.format("%s", observeSite.getObserveSiteId()));
        dict2.add(observeSite);
    }

    public long getSize(long longChunk, long latiChunk){
//        long id = getIdByLongLatiChunk(longChunk, latiChunk);
//        if(dict.containsKey(id))
//            return dict.get(id).size();
//        else
//            return 0;
        return dict2.size();
    }

    public long getSize(long id){
//        log.info(String.format("id: %d", id));
//        if(dict.containsKey(id))
//            return dict.get(id).size();
//        else
//            return 0;
        return dict2.size();
    }

    /**
     * 특정 id에 저장되어 있는 observeSite들을 별점 순으로 정렬하기
     * @param longChunk 경도 청크 id
     * @param latiChunk 위도 청크 id
     *
     * @return 아무것도 반환하지 않음
     */
    public void sortSitesById(long longChunk, long latiChunk){
//        long id = Long.valueOf(getIdByLongLatiChunk(longChunk, latiChunk));
//        List<ObserveSite> sites = dict.get(id);
//        for(ObserveSite site : sites){
//            log.info(String.format("Observe %s", site.getObserveSiteId()));
//        }
//        sites.sort(observeSiteSorter);
//
        dict2.sort(observeSiteSorter);
    }

    /**
     * 청크별로 모든 observeSite들을 별점 순으로 정렬.
     *
     * @return 아무것도 반환하지 않음
     */
    public void sortAllSites(){
//        for(Long id: dict.keySet()){
//            List<ObserveSite> sites = dict.get(id);
//            sites.sort(observeSiteSorter);
//        }
        dict2.sort(observeSiteSorter);
    }

    public void updateObserveSite(long longChunk, long latiChunk, ObserveSite observeSite){
//        long id = Long.valueOf(getIdByLongLatiChunk(longChunk, latiChunk));
//        List<ObserveSite> sites = dict.get(id);
//
//        String observeId = observeSite.getObserveSiteId();
//
//        for(ObserveSite site: sites){
//            if(site.getObserveSiteId().equals(observeId)){
//                site = observeSite;
//                return;
//            }
//        }

        for(ObserveSite site: dict2){
            var observeId = site.getObserveSiteId();
            if ((abs(site.getLongitude() - observeSite.getLongitude()) < 1.0E-5) || (abs(site.getLatitude() - observeSite.getLatitude()) < 1.0E-5)){
                site = observeSite;
                return;
            }
        }

        throw new CustomException(ErrorCode.SERVER_ERROR);
    }

    public ObserveSite getObserveSite(long id, int order){
        return dict.get(id).get(order);
    }

    public int getTopObserveSite(List<Long> keys, List<Integer> orders){
        int idx = 0;
        Long topKey = keys.get(0);
        int topOrder = orders.get(0);

        for(int i = 1; i < keys.size(); i++){
            Long key = keys.get(i);
            int order = orders.get(i);

            if(observeSiteSorter.compare(dict.get(topKey).get(topOrder), dict.get(key).get(order)) > 0){
                idx = i;
                topKey = key;
                topOrder = order;
            }
        }
        return idx;
    }

    public List<ObserveSite> getTopObserveSite(Double startX, Double endX, Double startY, Double endY){
        int i = 0;

        List<ObserveSite> res = new ArrayList<>();

        for(ObserveSite observeSite: dict2){
            var lat = observeSite.getLatitude();
            var lng = observeSite.getLongitude();

            if(startX > lat || endX < lat || startY > lng || endY < lng){
                continue;
            }
            i++;
            res.add(observeSite);
            if(i >= maximumObserveSite){
                break;
            }
        }
        return res;
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
