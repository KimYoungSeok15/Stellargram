package com.instargram101.member.service;

import com.instargram101.global.common.exception.customException.CustomException;
import com.instargram101.member.dto.response.FindMemberResponseDto;
import com.instargram101.member.entity.Follow;
import com.instargram101.member.entity.Member;
import com.instargram101.member.exception.FollowErrorCode;
import com.instargram101.member.exception.MemberErrorCode;
import com.instargram101.member.repoository.FollowRepository;
import com.instargram101.member.repoository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    public FindMemberResponseDto findFollow(Long follower, Long followee) {
        Optional<Member> member = Optional.ofNullable(memberRepository.findByMemberIdAndActivated(followee, true)
                .orElseThrow(() -> new CustomException(MemberErrorCode.Member_Not_Found)));
        Optional<Follow> follow = followRepository.findByFollowerIdAndFolloweeId(follower, followee);
        FindMemberResponseDto responseDto = FindMemberResponseDto.builder()
                .memberId(member.get().getMemberId())
                .nickname(member.get().getNickname())
                .profileImageUrl(member.get().getProfileImageUrl())
                .isFollow(!follow.isEmpty())
                .followCount(member.get().getFollowCount())
                .followingCount(member.get().getFollowingCount())
                .build();
       return responseDto;
    }

    @Override
    public List<FindMemberResponseDto> findMembers(Long myId, List<Member> memberList) {
        List<FindMemberResponseDto> response = new ArrayList<>();
        if(memberList != null) {
            for(Member member : memberList) {
                Optional<Follow> follow = followRepository.findByFollowerIdAndFolloweeId(myId, member.getMemberId());
                FindMemberResponseDto responseDto = FindMemberResponseDto.builder()
                        .memberId(member.getMemberId())
                        .nickname(member.getNickname())
                        .profileImageUrl(member.getProfileImageUrl())
                        .isFollow(!follow.isEmpty())
                        .followCount(member.getFollowCount())
                        .followingCount(member.getFollowingCount())
                        .build();
                response.add(responseDto);
            }
        }

        return response;
    }


    public Boolean followUser(Long follower, Long followee) {
        Member myInfo = memberRepository.findByMemberIdAndActivated(follower,true)
                .orElseThrow(() -> new CustomException(FollowErrorCode.FOLLOWER_Not_Found));
        Member followingInfo = memberRepository.findByMemberIdAndActivated(followee, true)
                .orElseThrow(() -> new CustomException(FollowErrorCode.FOLLOWEE_Not_Found));
        if(myInfo.getMemberId() != followingInfo.getMemberId()) {
            followRepository.save(Follow.builder().followee(followingInfo).follower(myInfo).build());
            setFollowingCount(myInfo, 1);
            setFollowCount(followingInfo, 1);
            return true;
        } else {
            throw new CustomException(FollowErrorCode.FOLLOW_NOT_SUCCESS);
        }

    }

    public Boolean deleteFollow(Long followerId, Long followeeId) {
        Member follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new CustomException(FollowErrorCode.FOLLOWER_Not_Found));
        Member followee = memberRepository.findById(followeeId)
                .orElseThrow(() -> new CustomException(FollowErrorCode.FOLLOWEE_Not_Found));
        Follow follow = followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)
                        .orElseThrow(() -> new CustomException(FollowErrorCode.FOLLOW_Not_Found));
        followRepository.delete(follow);
        setFollowCount(followee, -1);
        setFollowingCount(follower, -1);
        return true;

    }

    public void setFollowingCount(Member member, int count) {
        member.setFollowingCount(member.getFollowingCount() + count);
        memberRepository.save(member);
    }

    public void setFollowCount(Member member, int count) {
        member.setFollowCount(member.getFollowCount() + count);
        memberRepository.save(member);
    }


    public List<Member> getFollowers(Long memberId) {
        return followRepository.findFollowersByFollowingId(memberId);
    }

    public List<Member> getFollowingMembers(Long memberId) {
        return followRepository.findFollowingMembersByFollowerId(memberId);
    }
}
