package com.instargram101.starcard.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SaveCardRequestDto {

    private String content;
    private LocalDateTime photoAt;
    private String category;
    private String tool;
    private String observeSiteId;
}