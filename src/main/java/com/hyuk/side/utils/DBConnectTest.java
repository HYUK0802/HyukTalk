package com.hyuk.side.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectTest {

    public static void main(String[] args) {
        // 데이터베이스 URL, 사용자 이름, 비밀번호 설정
        String jdbcUrl = "jdbc:mariadb://localhost:3306"; // DB URL
        String dbUser = "study"; // DB 사용자 이름
        String dbPassword = "test1234"; // DB 비밀번호

        // 연결 테스트
        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            if (connection != null) {
                System.out.println("데이터베이스에 성공적으로 연결되었습니다!");
            } else {
                System.out.println("데이터베이스 연결에 실패했습니다.");
            }
        } catch (SQLException e) {
            System.err.println("데이터베이스 연결 중 오류 발생:");
            e.printStackTrace();
        }
    }
}
