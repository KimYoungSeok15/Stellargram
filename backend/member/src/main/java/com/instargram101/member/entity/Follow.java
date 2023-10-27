package com.instargram101.member.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "follow")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followee", referencedColumnName = "member_id")
    private Member followee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower", referencedColumnName = "member_id")
    private Member follower;

}
