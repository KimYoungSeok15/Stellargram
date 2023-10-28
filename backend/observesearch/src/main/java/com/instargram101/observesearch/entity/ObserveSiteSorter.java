package com.instargram101.observesearch.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class ObserveSiteSorter implements Comparator<ObserveSite>{

    @Value("${value.minimum_review_count}")
    private Long minimumReviewCount;

    @Override
    public int compare(ObserveSite o1, ObserveSite o2) {
        //review Count가 0인 것들은 가장 최하위
        if(o1.getReviewCount() == 0 || o2.getReviewCount() == 0) return (int)(o2.getReviewCount() - o1.getReviewCount());
        // 통분했을 때 분자의 값.
        long numerator = o2.getRatingSum() * o1.getReviewCount() - o1.getRatingSum() * o2.getReviewCount();
        // 두 리뷰가 모두 최소 리뷰 수 초과가 되거나 미만이 되는 경우는 평균 별점으로 순서를 매김.
        // 그렇지 않은 경우 리뷰 수가 더 많은 관측지가 더 높은 순위를 가져감.
        if(o1.getReviewCount() > minimumReviewCount && o2.getReviewCount() > minimumReviewCount
        || o1.getReviewCount() < minimumReviewCount && o2.getReviewCount() < minimumReviewCount){
            if(numerator > 0) return 1;
            else if (numerator < 0) return -1;
            else{
                return (int)(o2.getReviewCount() - o1.getReviewCount());
            }
        }
        else{
            return (int)(o2.getReviewCount() - o1.getReviewCount());
        }
    }

}
