package com.instargram101.member.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "favorite")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liker", referencedColumnName = "member_id")
    private Member liker;

    @Column(nullable = false)
    private Long starId;
}
