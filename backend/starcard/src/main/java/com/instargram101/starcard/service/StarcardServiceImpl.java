package com.instargram101.starcard.service;

import com.instargram101.global.common.exception.customException.CustomException;
import com.instargram101.global.utils.S3Util;
import com.instargram101.starcard.dto.request.SaveCardRequestDto;
import com.instargram101.starcard.dto.response.FindCardsResponseDto;
import com.instargram101.starcard.dto.response.StarcardElement;
import com.instargram101.starcard.entity.Starcard;
import com.instargram101.starcard.entity.enums.StarcardCategory;
import com.instargram101.starcard.exception.StarcardErrorCode;
import com.instargram101.starcard.repoository.StarcardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StarcardServiceImpl implements StarcardService {

    private final StarcardRepository starcardRepository;

    private final S3Util s3Util;

    @Override
    public Long saveCard(Long myId, SaveCardRequestDto requestDto, MultipartFile imageFile) throws IOException {

        String dirName = "starcard_image";
        Map<String, String> result = s3Util.uploadFile(imageFile, dirName);

        String url = result.get("url");
        String fileName = result.get("fileName");

        StarcardCategory category;
        try {
            category = StarcardCategory.valueOf(requestDto.getCategory().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(StarcardErrorCode.Starcard_Not_Found_Category);
        }

        Starcard starcard = Starcard.builder()
                .memberId(myId)
                .imagePath(fileName)
                .imageUrl(url)
                .content(requestDto.getContent())
                .photoAt(requestDto.getPhotoAt())
                .category(category)
                .tools(requestDto.getTool())
                .observeSiteId(requestDto.getObserveSiteId())
                .build();

        starcardRepository.save(starcard);
        return starcard.getCardId();
    };

    @Override
    public FindCardsResponseDto findCards(Long myId, Long memberId) {
        List<StarcardElement> starcards = starcardRepository.findAllCardsWithLikeStatus(myId, memberId);
        return FindCardsResponseDto.of(starcards);
    }

    @Override
    public Long deleteCard(Long myId, Long cardId) {

        Starcard starcard = starcardRepository.findById(cardId)
                .orElseThrow(()-> new CustomException((StarcardErrorCode.Starcard_Not_Found)));
        if(starcard.getMemberId() != myId) {
            throw new CustomException(StarcardErrorCode.Starcard_Forbidden);
        }
        starcardRepository.deleteById(cardId);
        return cardId;
    }

    @Override
    public FindCardsResponseDto searchCards(Long myId, String keyword, String categoryString) {

        StarcardCategory category;
        try {
            category = StarcardCategory.valueOf(categoryString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(StarcardErrorCode.Starcard_Not_Found_Category);
        }
        List<StarcardElement> starcards = starcardRepository.findByKeywordAndCategory(myId, keyword, category);
        return FindCardsResponseDto.of(starcards);
    }

    @Override
    public FindCardsResponseDto findLikeCards(Long myId, Long memberId) {

        List<StarcardElement> starcards =starcardRepository.findCardsLikedByUser(myId, memberId);
        return FindCardsResponseDto.of(starcards);
    }

    @Override
    public List<Long> findLikeedMembers(Long cardId) {
        List<Long> memberIds = starcardRepository.findLikedMembersByCardId(cardId);
        return memberIds;
    }
}