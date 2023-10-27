package com.instargram101.observesite.dto.request;

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
public class ObserveSiteInfoRequestDto {

    @NotNull
    private Float latitude;

    @NotNull
    private Float longitude;

    @NotBlank
    private String name;
}
