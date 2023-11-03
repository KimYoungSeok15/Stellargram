package com.instargram101.chat.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection = "message")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ChatMessage {

    @Id
    private String id;

    private Long seq;

    private Long unixTimestamp;

    private String content;

    private Long memberId;

    private Long roomId;

}
