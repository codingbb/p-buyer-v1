package com.example.pbuyerv1.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;
    private final HttpSession session;

    //로그인
    @PostMapping("/login")
    public String login(UserRequest.LoginDTO requestDTO) {
        User sessionUser = userService.login(requestDTO);
        session.setAttribute("sessionUser", sessionUser);
        return "redirect:/";
    }


    @GetMapping("/login-form")
    public String loginForm() {

        return "/user/login-form";
    }

    //회원가입
    @PostMapping("/join")
    public String join(UserRequest.JoinDTO requestDTO) {
        userService.save(requestDTO);
        return "redirect:/";
    }

    @GetMapping("/user-join-form")
    public String joinForm() {

        return "/user/user-join-form";
    }

    @GetMapping("/user-update-form")
    public String updateForm() {

        return "/user/user-update-form";
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }

}
