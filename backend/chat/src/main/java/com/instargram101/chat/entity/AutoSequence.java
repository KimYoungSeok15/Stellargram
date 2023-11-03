package com.instargram101.chat.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection = "counters")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AutoSequence {

    @Id
    private String id;

    private Long seq;

    public void increaseSeq(){
        this.seq++;
    }
}
