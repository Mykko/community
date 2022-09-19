package life.majiang.community.service;

import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.model.UserExample;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by codedrinker on 2019/5/23.
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    private final CharsetEncoder ios8859_1Encoder = StandardCharsets.ISO_8859_1.newEncoder();


    @Transactional
    public String createUser(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0) {
            // 插入
            user.setAvatarUrl("/images/default-avatar.png");
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            users = userMapper.selectByExample(userExample);
            BeanUtils.copyProperties(users.get(0), user);
        } else {
            return "accountexists";
        }
        return "success";
    }

    public String doSigUp(User user) {

        String accountId = user.getAccountId();
        String name = user.getName();
        String password = user.getToken();

//        if (System.currentTimeMillis() - Long.parseLong(info.getTime()) > TIME_OUT) {
//            return "error";
//        }

//        if (ConfigureReader.instance().foundAccount(info.getAccount())) {
//            return "accountexists";
//        }

        if (accountId != null && accountId.length() >= 3 && accountId.length() <= 32
                && ios8859_1Encoder.canEncode(accountId)) {
            if (accountId.indexOf("=") < 0 && accountId.indexOf(":") < 0 && accountId.indexOf("#") != 0) {
                if (password != null && password.length() >= 3 && password.length() <= 32
                        && ios8859_1Encoder.canEncode(password)) {
                    return createUser(user);
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


    public String login(User userLogin) {

        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(userLogin.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0) {
            return "invalidaccount";
        } else {
            User user = users.get(0);
            if (user.getToken().equals(userLogin.getToken())) {
                BeanUtils.copyProperties(user, userLogin);
                return "success";
            }
            return "invalidpwd";
        }
    }
}
