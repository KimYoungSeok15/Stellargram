package com.instargram101.member.service;

import com.instargram101.member.dto.request.SignMemberRequestDto;
import com.instargram101.member.entity.Member;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MemberService {

    Boolean checkMember(Long memberId);
    Member createMember(Long memberId, SignMemberRequestDto request);
    Boolean checkNickname(String nickname);
    Member searchMember(Long memberId);
    Member updateNickname(Long memberId, String nickname);
    Boolean deleteMember(Long memberId);
    Long getMemberIdByNickname(String nickname);
    Member updateProfileImage(Long memberId, MultipartFile imageFile) throws IOException;
    List<Member> searchMembersByNickname(String searchNickname);
    List<Long> getMemberIdsByCardId(Long cardId);
    List<Member> getMembersByMemberIds(List<Long> memberIds);


}
