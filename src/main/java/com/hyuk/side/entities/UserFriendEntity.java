package com.hyuk.side.entities;

import lombok.Data;

import java.util.Date;

@Data
public class UserFriendEntity {
    private String userEmail;
    private String friendEmail;
    private String name;
    private String profileImage;
    private Date birth;
    private String status;




    public UserFriendEntity(String userEmail, String friendEmail) {
        this.userEmail = userEmail;
        this.friendEmail = friendEmail;
    }


}
