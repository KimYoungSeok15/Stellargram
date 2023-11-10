package com.instargram101.member.api;

import com.instargram101.global.common.exception.customException.CustomException;
import com.instargram101.member.entity.Member;
import com.instargram101.member.exception.MemberErrorCode;
import com.instargram101.member.repoository.MemberRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
public class MemberAspect {
    private final MemberRepository memberRepository;

    public MemberAspect(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Before("execution(* com.instargram101.member.api.MemberController.*(..)) && " +
            "!(execution(* com.instargram101.member.api.MemberController.checkById(..)) || " +
            "execution(* com.instargram101.member.api.MemberController.createMember(..)) || " +
            "execution(* com.instargram101.member.api.MemberController.checkByNickname(..)))")
    public void beforeApiCall(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        if(method.isAnnotationPresent(RequestHeader.class)) {
            RequestHeader requestHeaderAnnotation = method.getAnnotation(RequestHeader.class);
            String headerName = requestHeaderAnnotation.value();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String myIdHeader = request.getHeader(headerName);

            if( myIdHeader != null) {
                Long myId = Long.parseLong(myIdHeader);
                Member member = memberRepository.findByMemberIdAndActivated(myId, true)
                        .orElseThrow(() -> new CustomException(MemberErrorCode.Invalid_User));
            }
        }
    }

}
