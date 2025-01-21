package com.hyuk.side.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class ChatRoomIdGenerator {

    public static String generateChatRoomId(String userEmail, String friendEmail) {
        try {
            // 이메일 정렬
            String[] emails = {userEmail, friendEmail};
            Arrays.sort(emails);

            // 정렬된 이메일 연결
            String combinedEmails = emails[0] + "|" + emails[1];

            // SHA-256 해시 생성
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(combinedEmails.getBytes(StandardCharsets.UTF_8));

            // 해시 값을 16진수 문자열로 변환
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("에러");
        }
    }
}
