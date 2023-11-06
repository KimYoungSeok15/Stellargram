package com.instargram101.member.api;

import com.instargram101.global.common.exception.customException.CustomException;
import com.instargram101.member.entity.Member;
import com.instargram101.member.exception.MemberErrorCode;
import com.instargram101.member.repoository.MemberRepository;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

@Aspect
@Component
public class MemberAspect {
    private final MemberRepository memberRepository;

    public MemberAspect(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Before("execution(* com.instargram101.member.api.MemberController.*.*(..)) && " +
            " !(execution(* com.instargram101.member.api.MemberController.checkById(..) ) )|| " +
            " !(execution(* com.instargram101.member.api.MemberController.createMember(..)) )|| " +
            " !(execution(* com.instargram101.member.api.MemberController.checkByNickname(..)))")
    public void beforeApiCall(@RequestHeader("myId") Long myId) {
        Member member = memberRepository.findByMemberIdAndActivated(myId, true)
                .orElseThrow(() -> new CustomException(MemberErrorCode.Invalid_User));
    }
}
