package com.instargram101.starcard.entity;

import com.instargram101.starcard.entity.enums.StarcardCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "starcard")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Starcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long cardId;

    @Column(columnDefinition = "varchar(20)", nullable = false)
    private Long memberId;

    @Column(columnDefinition = "varchar(20)", nullable = false)
    private String observeSiteId;

    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String imagePath;

    @Column(columnDefinition = "varchar(200)", nullable = false)
    private String imageUrl;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StarcardLike> starcardLikes = new ArrayList<>();

    @Column(columnDefinition = "varchar(300)")
    private String content;

    @Column(name = "photo_at")
    private LocalDateTime photoAt;

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private StarcardCategory category;

    @Column(columnDefinition = "varchar(100)", nullable = true)
    private String tools;

    @Column
    private int likeCount;
}