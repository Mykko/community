package life.majiang.community.interceptor;

import life.majiang.community.enums.AdPosEnum;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.exception.ICustomizeErrorCode;
import life.majiang.community.model.User;
import life.majiang.community.model.UserExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Date: 2022/09/20 00:11
 * @Author: mykko
 */

@Service
public class AuthorizeInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        return true;
    }
}
