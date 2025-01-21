package com.hyuk.side.entities;

import lombok.Data;

import java.util.Date;
@Data
public class ChatRoomMembersEntity {
    private String chatRoomId;
    private String userEmail;
    private Date joinedAt;

    public ChatRoomMembersEntity(String chatRoomId, String userEmail, Date joinedAt) {
        this.chatRoomId = chatRoomId;
        this.userEmail = userEmail;
        this.joinedAt = joinedAt;
    }
}
