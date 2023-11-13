package com.instargram101.observesearch.dto.response;

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
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotBlank
    private String name;

    @NotBlank
    private Long reviewCount;

    @NotBlank
    private Long ratingSum;

}
