package com.instargram101.starcard.dto.response;

import com.instargram101.starcard.entity.Starcard;
import com.instargram101.starcard.entity.StarcardLike;
import com.instargram101.starcard.entity.enums.StarcardCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StarcardElement {

    private Long cardId;
    private Long memberId;
    private String observeSiteId;
    private String imagePath;
    private String content;
    private LocalDateTime photoAt;
    private Enum<StarcardCategory> category;
    private String tools;
    private int likeCount;
    private boolean amILikeThis;

    public static StarcardElement of(Starcard starcard){

        return StarcardElement.builder()
                .cardId(starcard.getCardId())
                .memberId(starcard.getMemberId())
                .observeSiteId(starcard.getObserveSiteId())
                .imagePath(starcard.getImagePath())
                .content(starcard.getContent())
                .photoAt(starcard.getPhotoAt())
                .category(starcard.getCategory())
                .tools(starcard.getTools())
                .likeCount(starcard.getLikeCount())
                .build();
    }
}