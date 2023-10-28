package com.instargram101.observesearch.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Comparator;

@Entity
@Table(name = "observe_site")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObserveSite{
    @Id
    @Column(unique = true, name = "observe_site_id", length = 20, nullable = false)
    private String observeSiteId;

    @Column(length = 20)
    private String name;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @ColumnDefault("0")
    private Long reviewCount;

    @ColumnDefault("0")
    private Long ratingSum;

    @Column
    private Long memberId;

}

