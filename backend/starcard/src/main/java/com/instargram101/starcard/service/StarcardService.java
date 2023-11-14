package com.instargram101.starcard.service;

import com.instargram101.starcard.dto.request.SaveCardRequestDto;
import com.instargram101.starcard.dto.response.FindCardResponseDto;
import com.instargram101.starcard.dto.response.FindCardsResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StarcardService {
    Long saveCard(Long myId, SaveCardRequestDto requestDto, MultipartFile imageFile) throws IOException;

    FindCardsResponseDto findCards(Long myId, Long memberID);

    Long deleteCard(Long myId, Long cardId);

    FindCardsResponseDto searchCards(Long myId, String keyword, String category);

    FindCardsResponseDto findLikeCards(Long myId, Long memberId);

    List<Long> findLikeedMembers(Long cardId);

    String likeCard(Long myId, Long cardId);

    FindCardResponseDto recommandCard(Long myId);
}