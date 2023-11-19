package com.instargram101.member.service;

import com.instargram101.global.common.exception.customException.CustomException;
import com.instargram101.global.common.response.CommonApiResponse;
import com.instargram101.member.dto.request.SignMemberRequestDto;
import com.instargram101.member.entity.Member;
import com.instargram101.member.exception.MemberErrorCode;
import com.instargram101.member.messagequeue.KafkaProducer;
import com.instargram101.member.repoository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.IOException;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final S3UploadService s3UploadService;
    private final CardServiceClient cardServiceClient;
    private final KafkaProducer kafkaProducer;


    public Boolean checkMember(Long memberId) {
        Optional<Member> member = memberRepository.findByMemberIdAndActivated(memberId, true);
        return !member.isEmpty();

    }

    public Member createMember(Long memberId, SignMemberRequestDto request) {
        String nickname = request.getNickname();
        String profileImageUrl = request.getProfileImageUrl();

        Member member = Member.builder()
                .memberId(memberId)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl.isEmpty() ? "https://stellagram-bucket-a101.s3.ap-northeast-2.amazonaws.com/profile_image/profile_stellagram.jpg" : profileImageUrl )
                .activated(true)
                .followCount(0L)
                .followingCount(0L)
                .cardCount(0L)
                .build();
        return memberRepository.save(member);
    }

    public Boolean checkNickname(String nickname) {
        return !memberRepository.existsByNicknameAndActivated(nickname, true);
    }

    public Member searchMember(Long memberId) {
        return memberRepository.findByMemberIdAndActivated(memberId, true)
                .orElseThrow(() -> new CustomException(MemberErrorCode.Member_Not_Found));
    }


    public Member updateNickname(Long memberId, String nickname) {
        Member member = memberRepository.findByMemberIdAndActivated(memberId, true)
                .orElseThrow(() -> new CustomException(MemberErrorCode.Member_Not_Found));
        member.setNickname(nickname);
        return memberRepository.save(member);

    }

    public Boolean deleteMember(Long memberId) {
        Member member = memberRepository.findByMemberIdAndActivated(memberId, true)
                .orElseThrow(() -> new CustomException(MemberErrorCode.Member_Not_Found));
        member.setActivated(false);
        memberRepository.save(member);

        kafkaProducer.send("test-topic", member);

        return true;
    }

    public Long getMemberIdByNickname(String nickname) {
        Member member = memberRepository.findByNicknameAndActivated(nickname, true)
                .orElseThrow(() -> new CustomException(MemberErrorCode.Member_Not_Found));
        return member.getMemberId();
    }

    public List<Member> searchMembersByNickname(String searchNickname) {
        return memberRepository.findByNicknameContainingAndActivated(searchNickname, true);
    }

    public Member updateProfileImage(Long memberId, MultipartFile imageFile) throws IOException  {
        String imageUrl = s3UploadService.saveFile(imageFile);
        Member member = memberRepository.findByMemberIdAndActivated(memberId, true)
                .orElseThrow(() -> new CustomException(MemberErrorCode.Member_Not_Found));
        member.setProfileImageUrl(imageUrl);
        return memberRepository.save(member);
    }

    public List<Long> getMemberIdsByCardId(Long cardId) {
        ResponseEntity<CommonApiResponse> response = cardServiceClient.getMemberIdsByCardId(cardId);
        return (List<Long>) response.getBody().getData();
    }

    public List<Member> getMembersByMemberIds(List<Long> memberIds) {
        List<Long> longMemberIds = new ArrayList<>();
        for (Object value : memberIds) {
            longMemberIds.add(Long.parseLong(""+value));
        }
        return memberRepository.findMembersByMemberIdInAndActivated(longMemberIds, true);
    }



}
