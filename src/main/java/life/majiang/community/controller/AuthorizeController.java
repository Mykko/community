package life.majiang.community.controller;

import life.majiang.community.model.User;
import life.majiang.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * Created by codedrinker on 2019/4/24.
 */
@Controller
@Slf4j
public class AuthorizeController {

    private static final String CHARSET_BY_AJAX = "text/html; charset=utf-8";

    @Resource
    private UserService userService;


    @RequestMapping(value = { "/authorize/doSigUp.ajax" }, produces = { CHARSET_BY_AJAX })
    @ResponseBody
    public String doSigUp(HttpServletRequest request, HttpSession session) {
        String accountId = request.getParameter("accountId");
        String password = request.getParameter("accountPwd");
        String name = request.getParameter("name");

        String reqVercode = request.getParameter("vercode");
        String trueVercode = (String) session.getAttribute("VERCODE");

        session.removeAttribute("VERCODE");

        // 验证码
        if (reqVercode == null || trueVercode == null || !trueVercode.equals(reqVercode.toLowerCase())) {
            return "needvercode";
        }

        User userSignUp = new User();
        userSignUp.setAccountId(accountId);
        userSignUp.setName(name);
        userSignUp.setToken(password);
        try {
            String msg = userService.doSigUp(userSignUp);
            session.setAttribute("user", userSignUp);
            return msg;
        } catch (Exception e) {
//            throw new RuntimeException(e);
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/authorize/doLogin.ajax",
            method = RequestMethod.POST,
            produces = {CHARSET_BY_AJAX})
    @ResponseBody
    public String doLogin(String accountId, String password, HttpSession session) {
        User user = new User();
        user.setAccountId(accountId);
        user.setToken(password);
        try {
            userService.login(user);
            session.setAttribute("user", user);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
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

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/register")
    public String register() {
        return "register";
    }
}
