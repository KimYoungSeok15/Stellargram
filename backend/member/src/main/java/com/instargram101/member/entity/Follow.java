package com.instargram101.member.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "follow")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower", referencedColumnName = "member_id")
    private Member follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followee", referencedColumnName = "member_id")
    private Member followee;

}
