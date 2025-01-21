package com.hyuk.side.entities;

import lombok.Data;

public class MemberEntity {
    @Data
    public static class ChatMember {
        private String name;
        private String profileImage;
    }
}
