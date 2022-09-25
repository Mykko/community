package life.majiang.community.service;

import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.model.UserExample;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class UserService {

    private final CharsetEncoder ios8859_1Encoder = StandardCharsets.ISO_8859_1.newEncoder();

    @Autowired
    private UserMapper userMapper;


    @Transactional
    public void crete(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0) {
            // 插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl("/images/default-avatar.png");
            userMapper.insert(user);
            users = userMapper.selectByExample(userExample);

            BeanUtils.copyProperties(users.get(0), user);

        } else {
            throw new RuntimeException("accountexists");
        }
    }

    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0) {
            // 插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        } else {
            //更新
            User dbUser = users.get(0);
            User updateUser = new User();
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            UserExample example = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
        }
    }

    public void login(User userLogin) {

        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(userLogin.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);

        if (users.size() == 0) {
            throw new RuntimeException("invalidaccount");
        } else {
            User user = users.get(0);
            if (user.getToken().equals(userLogin.getToken())) {
                BeanUtils.copyProperties(user, userLogin);
            } else {
                throw new RuntimeException("invalidpwd");
            }
        }
        return;
    }

    public String doSigUp(User userSigUp) {


        // 注册
        String accountId = userSigUp.getAccountId();
        String name = userSigUp.getName();
        String password = userSigUp.getToken();
        
        if (accountId != null && accountId.length() >= 3 && accountId.length() <= 32
                && ios8859_1Encoder.canEncode(accountId)) {
            if (accountId.indexOf("=") < 0 && accountId.indexOf(":") < 0 && accountId.indexOf("#") != 0) {
                if (password != null && password.length() >= 3 && password.length() <= 32
                        && ios8859_1Encoder.canEncode(password)) {
                    crete(userSigUp);
                    return "success";
                } else {
                    return "invalidpwd";
                }
            } else {
                return "illegalaccount";
            }
        } else {
            return "invalidaccount";
        }
    }


    public Boolean getLoginStatus(HttpSession session) {
        return session.getAttribute("user") != null;
    }

}
