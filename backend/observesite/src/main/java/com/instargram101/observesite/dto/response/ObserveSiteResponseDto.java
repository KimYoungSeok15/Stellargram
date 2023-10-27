package com.instargram101.observesite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ObserveSiteResponseDto {

    @NotNull
    private Float latitude;

    @NotNull
    private Float longitude;

    @NotBlank
    private String name;

    @NotBlank
    private Long reviewCount;

    @NotBlank
    private Long ratingSum;

}
