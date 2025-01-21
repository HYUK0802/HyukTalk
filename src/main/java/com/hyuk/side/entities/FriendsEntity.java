package com.hyuk.side.entities;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FriendsEntity {
    private String userEmail;
    private String friendEmail;
    private String status;             // 요청 상태 (PENDING, APPROVED, BLOCKED)
    private LocalDateTime requestedAt; // 요청 발생 시간
}
