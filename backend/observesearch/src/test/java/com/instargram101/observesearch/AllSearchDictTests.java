package com.instargram101.observesearch;

import com.instargram101.observesearch.entity.AllSearchDict;
import com.instargram101.observesearch.entity.ObserveSite;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AllSearchDictTests {

    @Autowired
    private AllSearchDict allSearchDict;

    @Test
    public void addDict(){
        ObserveSite o1 = ObserveSite
                .builder()
                .latitude(1.000)
                .longitude(1.0000)
                .ratingSum(15L)
                .reviewCount(3L)
                .build();
        ObserveSite o2 = ObserveSite
                .builder()
                .latitude(1.000)
                .longitude(1.0000)
                .ratingSum(14L)
                .reviewCount(4L)
                .build();
        ObserveSite o3 = ObserveSite
                .builder()
                .latitude(1.000)
                .longitude(2.0000)
                .ratingSum(13L)
                .reviewCount(5L)
                .build();
        ObserveSite o4 = ObserveSite
                .builder()
                .latitude(1.000)
                .longitude(1.0000)
                .ratingSum(13L)
                .reviewCount(5L)
                .build();
        allSearchDict.addObserveSite(1,1, o1);
        allSearchDict.addObserveSite(1,1, o2);
        allSearchDict.addObserveSite(1,2, o3);
        allSearchDict.addObserveSite(1,1, o4);

        System.out.println(allSearchDict.toString());
    }

    @Test
    public void toStringTest(){
        ObserveSite o1 = ObserveSite
                .builder()
                .latitude(1.000)
                .longitude(1.0000)
                .ratingSum(15L)
                .reviewCount(3L)
                .build();
        System.out.println(o1.toString());
    }

    @Test
    public void sorterTest(){
        ObserveSite o1 = ObserveSite
                .builder()
                .latitude(1.000)
                .longitude(1.0000)
                .ratingSum(15L)
                .reviewCount(3L)
                .build();
        ObserveSite o2 = ObserveSite
                .builder()
                .latitude(1.000)
                .longitude(1.0000)
                .ratingSum(14L)
                .reviewCount(4L)
                .build();
        ObserveSite o3 = ObserveSite
                .builder()
                .latitude(1.000)
                .longitude(2.0000)
                .ratingSum(13L)
                .reviewCount(5L)
                .build();
        ObserveSite o4 = ObserveSite
                .builder()
                .latitude(1.000)
                .longitude(1.0000)
                .ratingSum(18L)
                .reviewCount(5L)
                .build();
        allSearchDict.addObserveSite(1,1, o1);
        allSearchDict.addObserveSite(1,1, o2);
        allSearchDict.addObserveSite(1,2, o3);
        allSearchDict.addObserveSite(1,1, o4);

        allSearchDict.sortSitesById(1, 1);
        System.out.println(allSearchDict.toString());

    }
}
