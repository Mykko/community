package life.majiang.community.controller;

import life.majiang.community.model.User;
import life.majiang.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by codedrinker on 2019/4/24.
 */
@Controller
@Slf4j
public class AuthorizeController {


    @Autowired
    private UserService userService;


    @PostMapping("/authorize/doSigUp.ajax" )
    @ResponseBody
    public String doSigUp(User user, HttpSession session) {
        String msg = userService.doSigUp(user);
        if (msg.equals("success")) {
            session.setAttribute("user", user);
        }
        return msg;
    }

    @PostMapping("/authorize/doLogin.ajax")
    @ResponseBody
    public String doLogin(User user, HttpSession session) {
        String msg = userService.login(user);
        if (msg.equals("success")) {
            session.setAttribute("user", user);
        }
        return msg;
    }



    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        request.getSession().invalidate();
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/";
    }
}
