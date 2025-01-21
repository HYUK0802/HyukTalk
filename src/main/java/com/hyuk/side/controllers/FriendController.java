package com.hyuk.side.controllers;

import com.hyuk.side.entities.FriendRequestDTO;
import com.hyuk.side.entities.FriendsEntity;
import com.hyuk.side.entities.UserEntity;
import com.hyuk.side.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class FriendController {
    private final UserService userService;

    public FriendController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/friends")
    public ModelAndView getFriends(HttpSession session, Model model) {
        String userEmail = (String) session.getAttribute("userEmail");
        UserEntity user = (UserEntity) session.getAttribute("user");

        // 세션에 아이디가 없을 경우 로그인 페이지로 리다이렉트
        if (user == null) {
            return new ModelAndView("redirect:/login");
        }

        // 생일 일주일 내로 남은 친구들 불러오기
        List<UserEntity> friendsBirth = userService.getFriendBirth(userEmail);
        // 친구 요청 목록 가져오기
//        List<FriendRequestDTO> friendRequests = userService.getPendingRequests(userEmail);

        List<FriendRequestDTO> friendRequests = userService.getPendingRequests(userEmail);
        // 로그로 데이터 확인
        friendRequests.forEach(request ->
                System.out.println("상태: " + request.getStatus() + ", 요청자: " + request.getRequesterEmail())
        );
        List<UserEntity> friends = userService.getApprovedFriends(userEmail);

        // 요청 시간을 포맷팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        friendRequests.forEach(request -> {
            if (request.getRequestedAt() != null) {
                request.setRequestedAtFormatted(request.getRequestedAt().format(formatter)); // 포맷팅된 요청 시간 저장
            }
        });

        // 친구 총 수 계산
        int friendCount = friends.size();

        // ModelAndView 생성
        ModelAndView modelAndView = new ModelAndView("friends"); // friends.html 뷰 이름
        modelAndView.addObject("friends", friends); // 뷰로 전달할 데이터
        modelAndView.addObject("friendsBirth", friendsBirth); // 생일 데이터 전달
        modelAndView.addObject("friendCount", friendCount); // 친구 총합 전달
        modelAndView.addObject("friendRequests", friendRequests); // 친구 요청 전달
        modelAndView.addObject("user", user); // 사용자 정보 전달
        modelAndView.addObject("userEmail", userEmail); // 사용자 이메일 전달
        return modelAndView;
    }

    @PostMapping("/friends/add")
    @ResponseBody
    public ResponseEntity<String> addFriend(@RequestBody Map<String, String> payload, HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        String friendEmail = payload.get("friendEmail");

        // 유효성 검사
        if (userEmail == null) {
            return ResponseEntity.badRequest().body("로그인 상태가 아닙니다.");
        }
        if (friendEmail == null || friendEmail.isEmpty()) {
            return ResponseEntity.badRequest().body("유효한 이메일을 입력해주세요.");
        }
        if (userEmail.equals(friendEmail)) {
            return ResponseEntity.badRequest().body("자신을 친구로 추가할 수 없습니다.");
        }

        // 친구 상태 확인
        if (userService.checkIfFriendExists(userEmail, friendEmail)) {
            return ResponseEntity.badRequest().body("이미 친구로 등록된 사용자입니다.");
        }
        if (!userService.checkIfUserExists(friendEmail)) {
            return ResponseEntity.badRequest().body("존재하지 않는 사용자입니다.");
        }

        try {
            // 친구 요청 보내기
            userService.addFriend(userEmail, friendEmail);
            return ResponseEntity.ok("친구 요청을 보냈습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("친구 요청에 실패했습니다.");
        }
    }
    @PostMapping("/friends/accept")
    @ResponseBody
    public ResponseEntity<String> acceptFriendRequest(@RequestBody Map<String, String> payload, HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        String requesterEmail = payload.get("requesterEmail");

        // 로직 처리
        if (userEmail == null) {
            return ResponseEntity.badRequest().body("로그인 상태가 아닙니다.");
        }

        userService.acceptFriendRequest(userEmail, requesterEmail);
        return ResponseEntity.ok("친구 요청을 수락했습니다.");
    }


    @PostMapping("/friends/block")
    @ResponseBody
    public ResponseEntity<String> blockUser(@RequestBody FriendsEntity friendsEntity, HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");

        if (userEmail == null) {
            return ResponseEntity.badRequest().body("로그인 상태가 아닙니다.");
        }

        try {
            userService.blockUser(userEmail, friendsEntity.getUserEmail());
            return ResponseEntity.ok("사용자를 차단했습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
