package com.hyuk.side.entities;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MessageEntity {
    private int messageId;
    private String chatRoomId;
    private String senderEmail;
    private String content;
    private String messageType;
    private Date createdAt;
    private boolean read;
    private String filePath;

    private String senderName; // 보낸사람
    private String senderProfileImage; // 보낸사람
    private Date messageDate; //
    private int unreadCount;
    private List<String> readByUsers;

    public MessageEntity() {

    }


}
