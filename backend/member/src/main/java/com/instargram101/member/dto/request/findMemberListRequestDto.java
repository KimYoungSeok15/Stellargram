package com.instargram101.member.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class findMemberListRequestDto {

    @NotNull
    private List<Long> memberIds;

}
