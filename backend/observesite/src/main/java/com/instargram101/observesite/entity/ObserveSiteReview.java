package com.instargram101.observesite.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "observe_site_review")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObserveSiteReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "observe_site_id", referencedColumnName = "observe_site_id")
    private ObserveSite observeSite;

    @Column(length = 100)
    private String content;

    @Column
    private Long rating;

    @Column
    private Long memberId;

    @Column
    private LocalDateTime createdAt;
}
