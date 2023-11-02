package com.instargram101.member.service;

import com.instargram101.global.common.exception.customException.CustomException;
import com.instargram101.member.dto.request.SignMemberRequestDto;
import com.instargram101.member.entity.Member;
import com.instargram101.member.exception.MemberErrorCode;
import com.instargram101.member.repoository.MemberRepository;
import com.instargram101.member.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.io.IOException;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final S3UploadService s3UploadService;


    public Boolean checkMember(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        return !member.isEmpty();

    }

    public Member createMember(Long memberId, SignMemberRequestDto request) {
        String nickname = request.getNickname();
        String profileImageUrl = request.getProfileImageUrl();

        Member member = Member.builder()
                .memberId(memberId)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .activated(true)
                .followCount(0L)
                .followingCount(0L)
                .cardCount(0L)
                .build();
        return memberRepository.save(member);
    }

    public Boolean checkNickname(String nickname) {
        return !memberRepository.existsByNickname(nickname);
    }

    public Member searchMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.Member_Not_Found));
    }


    public Member updateNickname(Long memberId, String nickname) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.Member_Not_Found));
        member.setNickname(nickname);
        return memberRepository.save(member);

    }

    public Boolean deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.Member_Not_Found));
        member.setActivated(false);
        memberRepository.save(member);
        return true;
    }

    public Long getMemberIdByNickname(String nickname) {
        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new CustomException(MemberErrorCode.Member_Not_Found));
        return member.getMemberId();
    }

    public List<Member> searchMembersByNickname(String searchNickname) {
        List<Member> members = memberRepository.findByNicknameContaining(searchNickname);
        return members;
    }

    public Member updateProfileImage(Long memberId, MultipartFile imageFile) throws IOException  {
        String imageUrl = s3UploadService.saveFile(imageFile);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.Member_Not_Found));
        member.setProfileImageUrl(imageUrl);
        return memberRepository.save(member);
    }
}
