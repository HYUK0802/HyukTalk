package com.hyuk.side.mappers;

import com.hyuk.side.entities.FriendRequestDTO;
import com.hyuk.side.entities.FriendsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FriendMapper {

    int checkIfFriendExists(@Param("userEmail") String userEmail, @Param("friendEmail") String friendEmail);

    // 친구 상태 업데이트
    void updateFriendStatus(@Param("userEmail") String userEmail,
                            @Param("friendEmail") String friendEmail,
                            @Param("status") String status);

    // 친구 관계 삽입
    void insertFriend(@Param("userEmail") String userEmail,
                      @Param("friendEmail") String friendEmail,
                      @Param("status") String status);

    List<FriendRequestDTO> getPendingFriendRequests(String userEmail);






}
