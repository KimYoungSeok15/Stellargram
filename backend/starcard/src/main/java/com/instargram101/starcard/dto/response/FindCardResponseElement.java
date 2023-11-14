package com.instargram101.starcard.dto.response;

import com.instargram101.starcard.dto.MemberDto;
import com.instargram101.starcard.dto.query.StarcardWithAmILikeQueryDto;
import com.instargram101.starcard.entity.enums.StarcardCategory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FindCardResponseElement {
    private Long cardId;
    private Long memberId;
    private String memberNickname;
    private String memberProfileImageUrl;
    private String observeSiteId;
    private String imagePath;
    private String imageUrl;
    private String content;
    private LocalDateTime photoAt;
    private Enum<StarcardCategory> category;
    private String tools;
    private int likeCount;
    private boolean amILikeThis;

    public static FindCardResponseElement of(StarcardWithAmILikeQueryDto starcardWithAmILikeQueryDto, MemberDto memberDto){

        return FindCardResponseElement.builder()
                .cardId(starcardWithAmILikeQueryDto.getCardId())
                .memberId(starcardWithAmILikeQueryDto.getMemberId())
                .observeSiteId(starcardWithAmILikeQueryDto.getObserveSiteId())
                .imagePath(starcardWithAmILikeQueryDto.getImagePath())
                .imageUrl(starcardWithAmILikeQueryDto.getImageUrl())
                .content(starcardWithAmILikeQueryDto.getContent())
                .photoAt(starcardWithAmILikeQueryDto.getPhotoAt())
                .category(starcardWithAmILikeQueryDto.getCategory())
                .tools(starcardWithAmILikeQueryDto.getTools())
                .likeCount(starcardWithAmILikeQueryDto.getLikeCount())
                .amILikeThis(starcardWithAmILikeQueryDto.isAmILikeThis())
                .memberNickname(memberDto.getNickname())
                .memberProfileImageUrl(memberDto.getProfileImageurl())
                .build();
    }
}
