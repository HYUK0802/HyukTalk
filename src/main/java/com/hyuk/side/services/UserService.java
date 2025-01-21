package com.hyuk.side.services;

import com.hyuk.side.entities.FriendRequestDTO;
import com.hyuk.side.entities.FriendsEntity;
import com.hyuk.side.entities.UserEntity;
import com.hyuk.side.enums.LoginResult;
import com.hyuk.side.enums.RegisterEnum;
import com.hyuk.side.mappers.FriendMapper;
import com.hyuk.side.mappers.UserMapper;
import com.hyuk.side.utils.CryptoUtil;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final FriendMapper friendMapper;

    public UserService(UserMapper userMapper, FriendMapper friendMapper) {
        this.userMapper = userMapper;
        this.friendMapper = friendMapper;
    }

    public RegisterEnum register(UserEntity user) throws NoSuchAlgorithmException {
        // 이메일 중복
        if (this.userMapper.selectEmail(user.getEmail()) != null) {
            return RegisterEnum.FAILURE_EMAIL_DUPLICATE;
        }
        // 전화번호 중복
        if (this.userMapper.selectPhone(user.getPhone()) != null) {
            return RegisterEnum.FAILURE_CONTACT_DUPLICATE;
        }

        String hashPwd = CryptoUtil.hashSha512(user.getPwd());
        user.setPwd(hashPwd);
        return this.userMapper.insertUser(user) > 0
                ? RegisterEnum.SUCCESS
                : RegisterEnum.FAILURE;
    }

    public LoginResult login(UserEntity user) throws NoSuchAlgorithmException {
        UserEntity existingUser = this.userMapper.selectEmail(user.getEmail());
        if (existingUser == null) {
            return LoginResult.FAILURE;
        }
        user.setPwd(CryptoUtil.hashSha512(user.getPwd()));
        if(!user.getPwd().equals(existingUser.getPwd())){
            return LoginResult.FAILURE;
        }
        user.setEmail(existingUser.getEmail())
                .setName(existingUser.getName())
                .setPhone(existingUser.getPhone())
                .setRegisterAt(existingUser.getRegisterAt())
                .setProfileImage(existingUser.getProfileImage())
                .setBirth(existingUser.getBirth());
        return LoginResult.SUCCESS;
    }

    public List<UserEntity> getFriendBirth(String userEmail) {
        return userMapper.getFriendBirth(userEmail);
    }

    public UserEntity selectEmail(String email) {
        return userMapper.selectEmail(email);
    }


    public void addFriend(String userEmail, String friendEmail) {
        if (userEmail.equals(friendEmail)) {
            throw new IllegalArgumentException("자기 자신을 친구로 추가할 수 없습니다.");
        }
        // 한쪽 방향 추가
        friendMapper.insertFriend(userEmail, friendEmail,"PENDING");
        friendMapper.insertFriend(friendEmail, userEmail,"REQUEST");

    }
    public boolean checkIfFriendExists(String userEmail, String friendEmail) {
        return friendMapper.checkIfFriendExists(userEmail, friendEmail) > 0;
    }

    public boolean checkIfUserExists(String email) {
        return userMapper.checkIfUserExists(email) > 0;
    }
    // 친구 신청
    public void acceptFriendRequest(String userEmail, String friendEmail) {
        // 친구 관계를 양방향으로 설정
        friendMapper.updateFriendStatus(userEmail, friendEmail, "APPROVED");
        friendMapper.insertFriend(friendEmail, userEmail, "APPROVED");
    }
    // 친구 차단
    public void blockUser(String userEmail, String friendEmail) {
        // 친구 요청을 삭제하거나 차단 상태로 변경
        friendMapper.updateFriendStatus(userEmail, friendEmail, "BLOCKED");
    }

    public List<FriendRequestDTO> getPendingRequests(String userEmail) {
        return friendMapper.getPendingFriendRequests(userEmail);
    }
    public List<UserEntity> getApprovedFriends(String userEmail) {
        return userMapper.getFriendsByUserEmail(userEmail);
    }
    public void updateProfileImage(String email, String imagePath) {
        System.out.println("Updating profile image for email: " + email + ", path: " + imagePath);
        userMapper.updateProfileImage(email, imagePath);
    }
    public void updateUserName(String email, String newName) {
        userMapper.updateUserName(email, newName);
    }


}
