package com.hyuk.side.entities;

import lombok.Data;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

//@Data
//public class ChatRoomsEntity {
//    private String chatRoomId;
//    private String type;
//    private Date createdAt;
//    private String name;
//    private String description;
//
//    // 추가 필드
//    private String friendName; // 대화 상대 이름
//    private String profileImage; // 대화 상대 프로필 이미지
//    private String lastMessage; // 마지막 메시지 내용
//    private Date lastUpdated; // 마지막 메시지 시간
//    private int unreadCount; // 읽지 않은 메시지 개수
//
//
//
//    public ChatRoomsEntity(String chatRoomId, String type) {
//        this.chatRoomId = chatRoomId;
//        this.type = type;
//        this.createdAt = createdAt;
//    }
@Data
public class ChatRoomsEntity {
    private String chatRoomId;
    private String type;
    private String name;
    private Date createdAt;
    private String lastMessage;
    private Date lastUpdated;
    private int unreadCount;
    private String profileImage;
    private String description;

    private List<String> friendNames; // 멤버 이름 리스트
    private List<String> profileImages; // 멤버 프로필 이미지 리스트



    public void setFriendNames(String concatenatedNames) {
        this.friendNames = Arrays.asList(concatenatedNames.split(","));
    }

    public void setProfileImages(String concatenatedImages) {
        this.profileImages = Arrays.asList(concatenatedImages.split(","));
    }
}

//}
