package com.instargram101.starcard.dto.query;

import com.instargram101.starcard.entity.Starcard;
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
public class StarcardWithAmILikeQueryDto {

    private Long cardId;
    private Long memberId;
    private String observeSiteId;
    private String imagePath;
    private String imageUrl;
    private String content;
    private LocalDateTime photoAt;
    private Enum<StarcardCategory> category;
    private String tools;
    private int likeCount;
    private boolean amILikeThis;

    public static StarcardWithAmILikeQueryDto from(Starcard starcard){

        return StarcardWithAmILikeQueryDto.builder()
                .cardId(starcard.getCardId())
                .memberId(starcard.getMemberId())
                .observeSiteId(starcard.getObserveSiteId())
                .imagePath(starcard.getImagePath())
                .imageUrl(starcard.getImageUrl())
                .content(starcard.getContent())
                .photoAt(starcard.getPhotoAt())
                .category(starcard.getCategory())
                .tools(starcard.getTools())
                .likeCount(starcard.getLikeCount())
                .build();
    }
}