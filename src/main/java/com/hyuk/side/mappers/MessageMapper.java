package com.hyuk.side.mappers;

import com.hyuk.side.entities.MessageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {
    void insertMessage(MessageEntity message);
    List<MessageEntity> getMessagesByChatRoomId(@Param("chatRoomId") String chatRoomId);
    void markMessagesAsRead(@Param("chatRoomId") String chatRoomId, @Param("userEmail") String userEmail);
    int countUnreadMessages(@Param("chatRoomId") String chatRoomId, @Param("userEmail") String userEmail);


}
