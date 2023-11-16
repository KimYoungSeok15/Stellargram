package com.instargram101.starcard.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.instargram101.global.common.exception.customException.CustomException;
import com.instargram101.global.common.response.CommonApiResponse;
import com.instargram101.global.utils.S3Util;
import com.instargram101.starcard.dto.MemberDto;
import com.instargram101.starcard.dto.request.MemberClientReqeustDto;
import com.instargram101.starcard.dto.request.SaveCardRequestDto;
import com.instargram101.starcard.dto.response.FindCardResponseDto;
import com.instargram101.starcard.dto.response.FindCardResponseElement;
import com.instargram101.starcard.dto.response.FindCardsResponseDto;
import com.instargram101.starcard.dto.query.StarcardWithAmILikeQueryDto;
import com.instargram101.starcard.entity.Starcard;
import com.instargram101.starcard.entity.StarcardLike;
import com.instargram101.starcard.entity.enums.StarcardCategory;
import com.instargram101.starcard.exception.StarcardErrorCode;
import com.instargram101.starcard.repoository.StarcardLikeRepository;
import com.instargram101.starcard.repoository.StarcardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class StarcardServiceImpl implements StarcardService {

    private final StarcardRepository starcardRepository;
    private final StarcardLikeRepository starcardLikeRepository;
    private final S3Util s3Util;
    private final MemberServiceClient memberServiceClient;

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

        List<StarcardWithAmILikeQueryDto> starcards = starcardRepository.findAllCardsWithLikeStatus(myId, memberId);
        List<FindCardResponseElement> res = getResponseByCardAndMember(starcards);

        return FindCardsResponseDto.of(res);
    }

    @Override
    public Long deleteCard(Long myId, Long cardId) {

        Starcard starcard = starcardRepository.findById(cardId)
                .orElseThrow(()-> new CustomException((StarcardErrorCode.Starcard_Not_Found)));
        if(!starcard.getMemberId().equals(myId)) {
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

        List<StarcardWithAmILikeQueryDto> starcards = starcardRepository.findByKeywordAndCategory(myId, keyword, category);
        List<FindCardResponseElement> res = getResponseByCardAndMember(starcards);

        return FindCardsResponseDto.of(res);
    }

    @Override
    public FindCardsResponseDto findLikeCards(Long myId, Long memberId) {

        List<StarcardWithAmILikeQueryDto> starcards = starcardRepository.findCardsLikedByUser(myId, memberId);
        List<FindCardResponseElement> res = getResponseByCardAndMember(starcards);

        return FindCardsResponseDto.of(res);
    }

    @Override
    public List<Long> findLikeedMembers(Long cardId) {
        List<Long> memberIds = starcardRepository.findLikedMembersByCardId(cardId);
        return memberIds;
    }

    @Override
    public String likeCard(Long myId, Long cardId) {
        AtomicReference<Boolean> notPresent = new AtomicReference<>(false);
        starcardLikeRepository.findByMemberIdAndCardId(myId, cardId).ifPresentOrElse(
                starcardLike -> {
                    starcardLikeRepository.delete(starcardLike);
                    Starcard starcard = starcardRepository.findById(cardId).get();
                    starcard.setLikeCount(starcard.getLikeCount()-1);
                    starcardRepository.save(starcard);
                },
                () ->{
                    notPresent.set(true);
                    Starcard starcard = starcardRepository.findById(cardId).orElseThrow(
                            ()-> new CustomException(StarcardErrorCode.Starcard_Not_Found));

                    StarcardLike starcardLike  = StarcardLike.builder()
                            .memberId(myId)
                            .card(starcard)
                            .build();

                    starcard.setLikeCount(starcard.getLikeCount()+1);
                    starcardRepository.save(starcard);
                    starcardLikeRepository.save(starcardLike);
                }
        );
        if (notPresent.get().equals(false)){
            return "좋아요해제완료";
        } else {
            return "좋아요설정완료";
        }
    }

    @Override
    public FindCardResponseDto recommandCard(Long myId) {
        StarcardWithAmILikeQueryDto starcard = starcardRepository.findOneRandomly(myId, PageRequest.of(0,1));
        List<StarcardWithAmILikeQueryDto> temp = new ArrayList<>();
        temp.add(starcard);
        FindCardResponseElement res = getResponseByCardAndMember(temp).get(0);
        return FindCardResponseDto.of(res);
    }

    private List<FindCardResponseElement>getResponseByCardAndMember(List<StarcardWithAmILikeQueryDto> starcards) {

        List<Long> memberIds = getMemberIdsFromCards(starcards);  // 카드 작성자 리스트 추출
        MemberClientReqeustDto memberClientReqeustDto = MemberClientReqeustDto.of(memberIds);   // feign 요청을 위한 request 객체

        ResponseEntity<CommonApiResponse> responseEntity = memberServiceClient.getMemberInfoByMemberIds(memberClientReqeustDto);    // feign

        List<MemberDto> memberInfos = getMemberInfoFromResponse(responseEntity);    // 응답을 파싱하여 회원정보만 추출
        log.info("회원조회: {}", memberInfos);

        List<FindCardResponseElement> res = new ArrayList<>();

        // 카드와 회원정보를 통해 응답형태 완성
        for(StarcardWithAmILikeQueryDto starcard : starcards){
            for(MemberDto member: memberInfos){
                if (Objects.equals(starcard.getMemberId(), member.getMemberId())) {
                    FindCardResponseElement findCardResponseElement = FindCardResponseElement.of(starcard,member);
                    res.add(findCardResponseElement);
                }
            }
        }
        return res;
    };

    private List<MemberDto> getMemberInfoFromResponse(ResponseEntity<CommonApiResponse> commonApiResponse) {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(commonApiResponse);
            // this.attributes객체를 ObjectMapper를 이용해서 json으로 바꿈
            JsonNode jsonNode = objectMapper.readTree(json);
            JsonNode data = jsonNode.get("body").get("data").get("members");

            List<MemberDto> result = new ArrayList<>();

            for(JsonNode dataElement : data){
                MemberDto memberDto = MemberDto.builder()
                        .nickname(dataElement.get("nickname").asText())
                        .profileImageurl(dataElement.get("profileImageUrl").asText())
                        .memberId(dataElement.get("memberId").asLong())
                        .build();
                result.add(memberDto);
            }

            return result;

        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace(); // 에러 메시지를 출력하거나 로그에 기록할 수 있음
            // 예외 발생 시 반환할 값 또는 예외 처리 방법을 선택
            return null;
        }
    };

    private List<Long> getMemberIdsFromCards(List<StarcardWithAmILikeQueryDto> starcards) {
        // 회원조회
        List<Long> memberIds = new ArrayList<>();
        for(StarcardWithAmILikeQueryDto starcard : starcards) {
            memberIds.add(starcard.getMemberId());
        }
        log.info("스타카드의 멤버아이디: {}", memberIds);
        return memberIds;
    };
}
