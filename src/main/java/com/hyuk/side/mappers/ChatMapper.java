package com.hyuk.side.mappers;

import com.hyuk.side.entities.ChatRoomMembersEntity;
import com.hyuk.side.entities.ChatRoomsEntity;
import com.hyuk.side.entities.MessageEntity;
import com.hyuk.side.entities.UserEntity;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMapper {


    void createChatRoom(ChatRoomsEntity chatRoom);

    ChatRoomsEntity getChatRoomById(@Param(value = "chatRoomId") String chatRoomId);
    void addUserToChatRoom(ChatRoomMembersEntity chatRoomMember);

    int checkUserInChatRoom(@Param("chatRoomId") String chatRoomId,
                            @Param("userEmail") String userEmail);
    List<ChatRoomsEntity> getChatRoomsByUserEmail(String userEmail);
    List<MessageEntity> getMessagesByChatRoomId(String chatRoomId);
    void addMessage(MessageEntity message);

    // 사용자 아이디 불러오기
    List<ChatRoomMembersEntity> getChatRoomMember(@Param(value = "chatRoomId") String chatRoomId,
                                                  @Param(value = "userEmail")String userEmail);
    // 사용자 추가 중복 확인
    boolean existsByChatRoomIdAndUserEmail(@Param("chatRoomId") String chatRoomId, @Param("userEmail") String userEmail);
    // 채팅방에 사용자가 존재하는지 확인

    // 친구의 이메일을 조회하는 메서드
    String getFriendEmail(@Param("chatRoomId") String chatRoomId, @Param("userEmail") String userEmail);

    // 사용자 정보를 조회하는 메서드
    UserEntity getUserInfo(@Param("email") String email);
    //대화 목록 조회
    List<ChatRoomsEntity> getChatRoomsByUser(@Param("userEmail") String userEmail);

    // 채팅방 ID로 참여자 이름 리스트 가져오기
    List<String> getParticipantNames(@Param("chatRoomId") String chatRoomId);

    void updateIsActive(
            @Param("userEmail") String userEmail,
            @Param("chatRoomId") String chatRoomId
    );
    void reactivateUserInChatRoom(
            @Param("userEmail") String userEmail,
            @Param("chatRoomId") String chatRoomId
    );
    void addMemberToChatRoom(
            @Param("userEmail") String userEmail,
            @Param("chatRoomId") String chatRoomId
    );
    boolean checkIfMemberExists(
            @Param("userEmail") String userEmail,
            @Param("chatRoomId") String chatRoomId
    );





}
