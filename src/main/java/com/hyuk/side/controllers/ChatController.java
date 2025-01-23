package com.hyuk.side.controllers;

import ch.qos.logback.core.model.Model;
import com.hyuk.side.entities.ChatRoomsEntity;
import com.hyuk.side.entities.MessageEntity;
import com.hyuk.side.entities.UserEntity;
import com.hyuk.side.entities.UserFriendEntity;
import com.hyuk.side.services.ChatService;
import com.hyuk.side.services.FileStorageService;
import com.hyuk.side.services.MessageService;
import com.hyuk.side.services.UserService;
import com.hyuk.side.utils.ChatRoomIdGenerator;
import com.hyuk.side.utils.CryptoUtil;
import com.hyuk.side.utils.HashUtil;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class ChatController {

    private final ChatService chatService;
    private final MessageService messageService;
    private final FileStorageService fileStorageService;
    private final UserService userService;

    public ChatController(ChatService chatService, MessageService messageService, FileStorageService fileStorageService, UserService userService) {
        this.chatService = chatService;
        this.messageService = messageService;
        this.fileStorageService = fileStorageService;
        this.userService = userService;
    }
    public static class GroupChatRequest {
        private String groupName;
        private List<String> selectedEmails;

        // Getters and Setters
        public String getGroupName() { return groupName; }
        public void setGroupName(String groupName) { this.groupName = groupName; }

        public List<String> getSelectedEmails() { return selectedEmails; }
        public void setSelectedEmails(List<String> selectedEmails) { this.selectedEmails = selectedEmails; }
    }





    @GetMapping("/chat/start")
    public ModelAndView startChat(@RequestParam("friendEmail") String friendEmail,
                                  @RequestParam("userEmail") String userEmail) throws NoSuchAlgorithmException {

        // 유저 이메일과 친구 이메일로 해시 생성

        String chatRoomId = ChatRoomIdGenerator.generateChatRoomId(userEmail, friendEmail);

        // 채팅방 생성 확인/처리
        System.out.println("Generated chatRoomId: " + chatRoomId);
        // 채팅방 생성 및 참여자 추가
        chatService.getOrCreateChatRoom(chatRoomId, userEmail, friendEmail);
        // 생성된 해시 값을 사용해 채팅방으로 이동
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/chat/" + chatRoomId);
        return mav;
    }


    @PostMapping("/chat/createGroup")
    @ResponseBody
    public ResponseEntity<String> createGroupChat(
            @RequestBody Map<String, Object> payload, // 클라이언트에서 전송된 데이터
            HttpSession session) {

        // 세션에서 사용자 이메일 가져오기
        String creatorEmail = (String) session.getAttribute("userEmail");
        if (creatorEmail == null) {
            return ResponseEntity.badRequest().body("Invalid user session.");
        }

        // 클라이언트에서 전달된 데이터 파싱
        String groupName = (String) payload.get("groupName");
        List<String> selectedEmails = (List<String>) payload.get("selectedEmails");

        // 유효성 검사
        if (groupName == null || groupName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("그룹이름을 정해주세요.");
        }
        if (selectedEmails == null || selectedEmails.isEmpty()) {
            return ResponseEntity.badRequest().body("한명 이상의 대화 상대를 정해주세요.");
        }

        // 그룹 생성
        try {
            String chatRoomId = chatService.createGroupChatRoom(groupName, selectedEmails, creatorEmail);
            return ResponseEntity.ok("/chat/" + chatRoomId); // 성공 시 대화방 URL 반환
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating group chat: " + e.getMessage());
        }
    }




    @GetMapping("/chat/{chatRoomId}")
    public ModelAndView chatRoom(
            @PathVariable("chatRoomId") String chatRoomId,
            HttpSession session) {
        // 세션에서 사용자 이메일 가져오기
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            return new ModelAndView("redirect:/login");
        }

        // 채팅방 정보 조회
        ChatRoomsEntity chatRoom = chatService.getChatRoomById(chatRoomId);
        // 채팅방 입장 처리
        chatService.enterChatRoom(userEmail, chatRoomId);
        // 메시지 가져오기
        List<MessageEntity> messages = messageService.getMessages(chatRoomId);

        // 친구 이메일 및 사용자 정보 가져오기
        String friendEmail = chatService.getFriendEmail(chatRoomId, userEmail);
        UserEntity user = chatService.getUserInfo(userEmail);
        UserEntity friend = chatService.getUserInfo(friendEmail);

        // 데이터 전달
        ModelAndView mav = new ModelAndView("chat");
        mav.addObject("chatRoomId", chatRoomId);
        mav.addObject("chatTitle", chatRoom.getName());
        mav.addObject("chatFriend", chatRoom.getFriendNames());
        mav.addObject("messages", messages);
        mav.addObject("userEmail", userEmail);
        mav.addObject("friendEmail", friendEmail);
        mav.addObject("userName", user.getName());
        mav.addObject("friendName", friend.getName());
        mav.addObject("friendImg", friend.getProfileImage());
        return mav;
    }
    @GetMapping("/chat/{chatRoomId}/unread-count")
    public ResponseEntity<Integer> getUnreadCount(
            @PathVariable String chatRoomId,
            HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");

        if (userEmail == null) {
            return ResponseEntity.badRequest().body(0);
        }

        int unreadCount = messageService.countUnreadMessages(chatRoomId, userEmail);
        return ResponseEntity.ok(unreadCount);
    }
    @PostMapping("/chat/{chatRoomId}/markAsRead")
    @ResponseBody
    public ResponseEntity<Void> markMessagesAsRead(
            @PathVariable("chatRoomId") String chatRoomId,
            HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        messageService.markMessagesAsRead(chatRoomId, userEmail);
        return ResponseEntity.ok().build();
    }



    @PostMapping("/chat/{chatRoomId}/send")
    @ResponseBody
    public ResponseEntity<String> sendTextMessage(
            @PathVariable("chatRoomId") String chatRoomId,
            @RequestParam(value = "content", required = true) String content,
            HttpSession session) {

        // 세션에서 사용자 이메일 가져오기
        String senderEmail = (String) session.getAttribute("userEmail");
        if (senderEmail == null) {
            return ResponseEntity.badRequest().body("다시 로그인 해주세요");
        }

        // 메시지 내용 검증
        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("메세지 오류입니다. 다시 시도해주세요");
        }

        // 메시지 저장
        messageService.saveMessage(chatRoomId, senderEmail, content, "TEXT", null);

        return ResponseEntity.ok("good");
    }

    @PostMapping("/chat/{chatRoomId}/upload")
    @ResponseBody
    public ResponseEntity<String> uploadFile(
            @PathVariable("chatRoomId") String chatRoomId,
            @RequestParam("file") MultipartFile file,
            HttpSession session) {

        // 사용자 이메일 가져오기
        String senderEmail = (String) session.getAttribute("userEmail");
        if (senderEmail == null) {
            return ResponseEntity.badRequest().body("다시 로그인 해주세요.");
        }

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("다시 시도해주세요.");
        }

        try {
            // 파일 저장
            String filePath = fileStorageService.storeChatImage(file);

            // DB에 메시지 저장
            messageService.saveMessage(chatRoomId, senderEmail, null, "IMAGE", filePath);

            return ResponseEntity.ok(filePath); // 반환된 filePath를 클라이언트에서 사용할 수 있도록 보냄
        } catch (Exception e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }
    @PostMapping("/chat/leave")
    @ResponseBody
    public ResponseEntity<String> leaveChatRoom(@RequestBody Map<String, String> payload, HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        String chatRoomId = payload.get("chatRoomId");

        if (userEmail == null) {
            return ResponseEntity.badRequest().body("로그인 상태가 아닙니다.");
        }

        try {
            chatService.leaveChatRoom(userEmail, chatRoomId);
            return ResponseEntity.ok("대화방에서 나갔습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("대화방 나가기에 실패했습니다.");
        }
    }

    @PostMapping("/profile/upload")
    @ResponseBody
    public ResponseEntity<String> uploadProfileImage(
            @RequestParam("profileImage") MultipartFile file,
            HttpSession session) {
        // 사용자 이메일 가져오기
        String userEmail = (String) session.getAttribute("userEmail");

        // 유효성 검사
        if (file.isEmpty() || userEmail == null) {
            return ResponseEntity.badRequest().body("잘못된 요청입니다.");
        }

        try {
            String filePath = fileStorageService.storeProfileImage(file); // 프로필 사진 저장
            userService.updateProfileImage(userEmail, filePath); // DB에 경로 저장
            return ResponseEntity.ok("프로필 사진 변경 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업로드 실패: " + e.getMessage());
        }
    }






}
