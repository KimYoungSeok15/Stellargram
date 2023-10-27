package com.instargram101.chat.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Document(collection = "message")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Message {

    @Id
    private Long id;

    private LocalDateTime sendAt;

    private String content;

    private Long memberId;

    private Long roomId;

}
