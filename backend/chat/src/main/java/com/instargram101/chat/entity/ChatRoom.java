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

    @Column(unique = true, name = "observe_site_id", length = 20, nullable = false)
    private String observeSiteId;

    public void increasePersonnel(){
        this.personnel++;
    }

    public void decreasePersonnel(){
        this.personnel--;
    }

}
