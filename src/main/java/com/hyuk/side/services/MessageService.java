package com.hyuk.side.services;

import com.hyuk.side.entities.MessageEntity;
import com.hyuk.side.mappers.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageMapper messageMapper;

    public void saveMessage(String chatRoomId, String senderEmail, String content, String messageType) {
        saveMessage(chatRoomId, senderEmail, content, messageType, null);
    }
    // 메시지 저장
    public void saveMessage(String chatRoomId, String senderEmail, String content, String messageType,String filePath) {
        MessageEntity message = new MessageEntity();
        message.setChatRoomId(chatRoomId);
        message.setSenderEmail(senderEmail);
        message.setContent(content);
        message.setMessageType(messageType);
        message.setFilePath(filePath);

        messageMapper.insertMessage(message);
    }

    // 메시지 조회
    public List<MessageEntity> getMessages(String chatRoomId) {
        return messageMapper.getMessagesByChatRoomId(chatRoomId);
    }

    // 메시지 읽음
    public void markMessagesAsRead(String chatRoomId, String userEmail) {
        messageMapper.markMessagesAsRead(chatRoomId, userEmail);
    }
    // 읽지않은 메세지
    public int countUnreadMessages(String chatRoomId, String userEmail) {
        return messageMapper.countUnreadMessages(chatRoomId, userEmail);
    }

}
