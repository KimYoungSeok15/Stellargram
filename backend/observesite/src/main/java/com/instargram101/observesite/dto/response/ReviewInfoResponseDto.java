package com.instargram101.observesite.dto.response;

import com.instargram101.observesite.entity.ObserveSite;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewInfoResponseDto {

    @NotBlank
    private Long reviewId;

    @NotBlank
    private String content;

    @NotBlank
    private Long rating;

    @NotBlank
    private LocalDateTime createdAt;

}
