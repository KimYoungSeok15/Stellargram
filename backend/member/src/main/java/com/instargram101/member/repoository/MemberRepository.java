package com.instargram101.member.repoository;

import com.instargram101.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long memberId);
    Boolean existsByNickname(String nickname);
    Optional<Member> findByNickname(String nickname);

    List<Member> findByNicknameContaining(String searchNickname);
}