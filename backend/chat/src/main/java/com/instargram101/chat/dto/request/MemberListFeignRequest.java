package com.instargram101.chat.dto.request;

import com.instargram101.chat.dto.response.MemberInfo;
import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberListFeignRequest {

    List<Long> memberIds;

    public static MemberListFeignRequest of(List<Long> memberIds) {
        return MemberListFeignRequest.builder()
                .memberIds(memberIds)
                .build();
    }
}
