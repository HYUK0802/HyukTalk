package com.hyuk.side.mappers;

import com.hyuk.side.entities.ChatRoomsEntity;
import com.hyuk.side.entities.FriendsEntity;
import com.hyuk.side.entities.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    int insertUser(UserEntity userEntity);

    UserEntity selectEmail(@Param(value = "email") String email);
    UserEntity selectPwd(@Param(value = "pwd") String pwd);
    UserEntity selectPhone(@Param(value = "phone") String phone);

    UserEntity selectEmailPwd(@Param(value = "email") String email,
                              @Param(value = "password") String password);

    List<UserEntity> getFriendsByUserEmail(@Param(value = "userEmail") String userEmail);

    List<UserEntity> getFriendBirth(@Param(value = "userEmail") String userEmail);

    int checkIfUserExists(@Param("email") String email);

    void updateProfileImage(@Param("email") String email,
                            @Param("imagePath") String imagePath);

    void updateUserName(@Param("email") String email,
                        @Param("newName")String newName);



}
