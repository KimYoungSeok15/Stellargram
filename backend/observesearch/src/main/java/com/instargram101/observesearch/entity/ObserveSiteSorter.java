package com.instargram101.observesearch.entity;

import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class ObserveSiteSorter implements Comparator<ObserveSite>{
    @Override
    public int compare(ObserveSite o1, ObserveSite o2) {
        long numerator = o1.getRatingSum() * o2.getReviewCount() - o2.getRatingSum() * o1.getReviewCount();
        return (numerator > 0) ? -1 : (numerator == 0) ? 0 : 1;
    }

}
