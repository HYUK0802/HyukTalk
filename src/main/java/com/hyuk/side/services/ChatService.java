package com.hyuk.side.services;

import com.hyuk.side.entities.ChatRoomMembersEntity;
import com.hyuk.side.entities.ChatRoomsEntity;
import com.hyuk.side.entities.UserEntity;
import com.hyuk.side.mappers.ChatMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ChatService {

        private final ChatMapper chatMapper;

        public ChatService(ChatMapper chatMapper) {
            this.chatMapper = chatMapper;
        }

    public ChatRoomsEntity getOrCreateChatRoom(String chatRoomId, String userEmail, String friendEmail) {
        // 채팅방 존재 여부 확인
        ChatRoomsEntity chatRoom = chatMapper.getChatRoomById(chatRoomId);

        if (chatRoom == null) {
            // 기본 생성자 사용
            chatRoom = new ChatRoomsEntity();
            chatRoom.setChatRoomId(chatRoomId);
            chatRoom.setType("1:1");
            chatMapper.createChatRoom(chatRoom); // chat_rooms에 삽입
        }

        // userEmail과 friendEmail을 모두 chat_room_members 테이블에 추가
        addUserToChatRoomIfNotExists(chatRoomId, userEmail);
        addUserToChatRoomIfNotExists(chatRoomId, friendEmail);

        return chatRoom;
    }
    public String createGroupChatRoom(String groupName, List<String> memberEmails, String creatorEmail) {
        // 그룹 대화방 ID 생성
        String chatRoomId = UUID.randomUUID().toString();

        // 대화방 생성
        ChatRoomsEntity chatRoom = new ChatRoomsEntity();
        chatRoom.setChatRoomId(chatRoomId);
        chatRoom.setType("GROUP");

        chatRoom.setFriendNames(groupName);
        chatMapper.createChatRoom(chatRoom);

        // 생성자 추가
        addUserToChatRoomIfNotExists(chatRoomId, creatorEmail);

        // 선택된 멤버 추가
        for (String email : memberEmails) {
            addUserToChatRoomIfNotExists(chatRoomId, email);
        }

        return chatRoomId;
    }
    public void addUserToChatRoomIfNotExists(String chatRoomId, String userEmail) {
        // 이미 추가된 멤버인지 확인
        if (!chatMapper.existsByChatRoomIdAndUserEmail(chatRoomId, userEmail)) {
            ChatRoomMembersEntity member = new ChatRoomMembersEntity(chatRoomId, userEmail, new Date());
            chatMapper.addUserToChatRoom(member);
        }
    }
    public ChatRoomsEntity getChatRoomById(String chatRoomId) {
        ChatRoomsEntity chatRoom = chatMapper.getChatRoomById(chatRoomId);

        // 그룹 대화방인 경우 참여자 이름 가져오기
        if ("GROUP".equals(chatRoom.getType())) {
            List<String> participantNames = chatMapper.getParticipantNames(chatRoomId);
            int totalParticipants = participantNames.size();
            if (totalParticipants > 2) {
                String displayedNames = String.join(", ", participantNames.subList(0, 2));
                chatRoom.setFriendNames(displayedNames + " 외 " + (totalParticipants - 2) + "명");
            } else {
                chatRoom.setFriendNames(String.join(", ", participantNames));
            }
        }
        return chatRoom;
    }
    public String getFriendEmail(String chatRoomId, String userEmail) {
        return chatMapper.getFriendEmail(chatRoomId, userEmail);
    }
    public UserEntity getUserInfo(String email) {
        return chatMapper.getUserInfo(email);
    }

    public List<ChatRoomsEntity> getChatRoomsByUser(String userEmail) {
        return chatMapper.getChatRoomsByUser(userEmail);
    }
    public void leaveChatRoom(String userEmail, String chatRoomId) {
        System.out.println("Leaving chat room for user: " + userEmail + ", chatRoomId: " + chatRoomId);

        chatMapper.updateIsActive(userEmail, chatRoomId); // is_active 값을 0으로 설정
        System.out.println("Chat room left successfully.");
    }
    public void enterChatRoom(String userEmail, String chatRoomId) {
        // 사용자가 이미 채팅방 멤버인지 확인하고 없으면 새로 추가 (필요한 경우)
        if (!chatMapper.checkIfMemberExists(userEmail, chatRoomId)) {
            chatMapper.addMemberToChatRoom(userEmail, chatRoomId); // 채팅방에 추가
        }

        // is_active 값을 1로 업데이트
        chatMapper.reactivateUserInChatRoom(userEmail, chatRoomId);
    }






}


