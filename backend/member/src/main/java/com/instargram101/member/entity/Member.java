package com.instargram101.member.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Objects;

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

    @Column(nullable = false)
    private boolean activated;

    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String profileImageUrl; //안 넣는 경우 base url로 넣어주기.

    @ColumnDefault("0")
    private Long followCount;

    @ColumnDefault("0")
    private Long followingCount;

    @ColumnDefault("0")
    private Long cardCount;

}
