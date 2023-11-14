package com.instargram101.member.repoository;

import com.instargram101.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberIdAndActivated(Long memberId, Boolean activated);
    Boolean existsByNicknameAndActivated(String nickname, Boolean activated);
    Optional<Member> findByNicknameAndActivated(String nickname, Boolean activated);
    List<Member> findByNicknameContainingAndActivated(String searchNickname, Boolean activated);
    List<Member> findMembersByMemberIdInAndActivated(List<Long> memberIds, Boolean activated);


}
