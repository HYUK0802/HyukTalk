package com.hyuk.side.controllers;

import com.hyuk.side.entities.UserEntity;
import com.hyuk.side.enums.LoginResult;
import com.hyuk.side.enums.RegisterEnum;
import com.hyuk.side.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping(value = "/")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // login 페이지 ////////
    @GetMapping(value = "/login")
    public ModelAndView getLogin() {
        ModelAndView mav = new ModelAndView("index");
        return mav;
    }
    @PostMapping(value = "/login")
    @ResponseBody
    public String postLogin(HttpSession session,
                            UserEntity user,
                            @RequestParam("email") String email) throws NoSuchAlgorithmException {
        LoginResult result = this.userService.login(user);
        if(result == LoginResult.SUCCESS){
            session.setAttribute("userEmail", email);
            session.setAttribute("user",user);
        }
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        return responseObject.toString();
    }
    // login 페이지 ////////

    // logout~~~~~~`
    @GetMapping(value = "logout")
    public ModelAndView getLogout(HttpSession session) {
        session.setAttribute("user",null);
        ModelAndView modelAndView = new ModelAndView("redirect:/login");
        return modelAndView;
    }
    // logout~~~~~~`


    // register 페이지 /////////
    @GetMapping(value = "/register")
    public ModelAndView getRegister() {
        ModelAndView mav = new ModelAndView("register");
        return mav;
    }
    @PostMapping(value = "/register")
    @ResponseBody
    public String postRegister(UserEntity user, @RequestParam(value = "birthStr") String birthStr) throws ParseException, NoSuchAlgorithmException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birth = sdf.parse(birthStr);
        user.setBirth(birth);
        user.setStatus("Good");
        user.setProfileImage("/img/profile.jpeg");
        RegisterEnum result = this.userService.register(user);
        JSONObject responseObject = new JSONObject(){{
            put("result", result.name().toLowerCase());
        }};
        return responseObject.toString();
    }
    // register 페이지 /////////

    @PostMapping("/profile/update-name")
    @ResponseBody
    public ResponseEntity<String> updateProfileName(@RequestBody Map<String, String> payload, HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        String newName = payload.get("newName");

        if (userEmail == null || newName == null || newName.isEmpty()) {
            return ResponseEntity.badRequest().body("잘못된 요청입니다.");
        }

        try {
            // DB에서 사용자 이름 업데이트
            userService.updateUserName(userEmail, newName);

            // 세션 데이터 업데이트
            session.setAttribute("userName", newName);

            return ResponseEntity.ok("이름이 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이름 변경 실패: " + e.getMessage());
        }
    }


    @GetMapping(value = "pr")
    public ModelAndView getSetting(){
        ModelAndView mv = new ModelAndView("more");
        return mv;
    }
}
