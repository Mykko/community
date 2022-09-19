package life.majiang.community.controller;


import life.majiang.community.service.UtilService;
import life.majiang.community.utils.VerificationCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Date: 2022/09/12 15:52
 * @Author: mykko
 */

@Controller
@RequestMapping("/util")
public class UtilController {

    @Resource
    private UtilService utilService;

    @RequestMapping("/getNewVerCode.do")
    public void getVerCode(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        VerificationCode vc = utilService.getNewVerCode(4);

        session.setAttribute("VERCODE", vc.getCode());
        response.setContentType("image/png");
        OutputStream out = response.getOutputStream();
        vc.saveTo(out);
        out.flush();
        out.close();
    }
}
