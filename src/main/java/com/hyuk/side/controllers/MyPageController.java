package com.hyuk.side.controllers;

import com.hyuk.side.entities.UserEntity;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MyPageController {
    @GetMapping("/setting")
    public ModelAndView MyPage(HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        UserEntity user = (UserEntity) session.getAttribute("user");

        if (userEmail == null) {
            return new ModelAndView("redirect:/login");
        }
        ModelAndView mv = new ModelAndView("more");
        mv.addObject("user", user);
        return mv;
    }
}
