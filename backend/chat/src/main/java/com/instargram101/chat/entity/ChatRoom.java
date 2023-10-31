package com.instargram101.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @Column
    private Long personnel;

//    // TODO: 관측소 아이디 테이블 잘못 만들어졌다는거 알려야함. Long이 아니라 String 해야 함
//    @Column
//    private Long observeSiteId;

    @Column(unique = true, name = "observe_site_id", length = 20, nullable = false)
    private String observeSiteId;

}
