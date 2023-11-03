package com.instargram101.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Member {

    @Id
    @Column(name = "member_id")
    private Long memberId;

    @Column(columnDefinition = "varchar(20)", nullable = false)
    private String nickname;

    @JsonIgnore
    @Column(nullable = false)
    private boolean activated;

    @Column(columnDefinition = "varchar(1000)")
    private String profileImageUrl;

    @ColumnDefault("0")
    private Long followCount;

    @ColumnDefault("0")
    private Long followingCount;

    @ColumnDefault("0")
    private Long cardCount;

}
