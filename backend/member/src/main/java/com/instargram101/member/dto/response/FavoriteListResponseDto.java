package com.instargram101.member.dto.response;

import com.instargram101.member.entity.Favorite;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteListResponseDto {

    @NotBlank
    private List<Favorite> favorites;
}
