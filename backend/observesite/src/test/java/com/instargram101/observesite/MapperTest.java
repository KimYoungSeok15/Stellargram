package com.instargram101.observesite;

import com.instargram101.observesite.dto.request.ObserveSiteInfoRequest;
import com.instargram101.observesite.mapper.ObserveSiteRequestMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MapperTest {

    private final ObserveSiteRequestMapper observeSiteRequestMapper = Mappers.getMapper(ObserveSiteRequestMapper.class);

    @Test
    void observeSiteMapper(){
        var request = ObserveSiteInfoRequest.builder()
                .name("ppsrac")
                .latitude(Float.valueOf("37.1234"))
                .longitude(Float.valueOf("127.1234"))
                .build();
        System.out.println("!");
        var observeSite = observeSiteRequestMapper.toEntity(request);
        observeSite.setRatingSum(Long.valueOf("0"));
        observeSite.setReviewCount(Long.valueOf("0"));

        System.out.println("id: " + observeSite.getObserveSiteId());
        System.out.println("name: " + observeSite.getName());
        System.out.println("latitude: " + observeSite.getLatitude().toString());
        System.out.println("longitude: " + observeSite.getLongitude().toString());
        System.out.println("review count: " + observeSite.getReviewCount().toString());
        System.out.println("Total rating: "+ observeSite.getRatingSum().toString());
    }
}
