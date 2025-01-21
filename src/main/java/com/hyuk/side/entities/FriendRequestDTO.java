package com.hyuk.side.entities;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class FriendRequestDTO {
    private String requesterEmail;   // 친구 요청을 보낸 사람 (userEmail과 동일)
    private String friendEmail;      // 친구 요청을 받은 사람
    private String name;
    private String status;           // 요청 상태 (PENDING, APPROVED, BLOCKED)
    private LocalDateTime requestedAt; // 요청 발생 시간
    private String requestedAtFormatted;
    private String profileImage;
}
