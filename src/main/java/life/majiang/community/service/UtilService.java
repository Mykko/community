package life.majiang.community.service;

import life.majiang.community.utils.VerificationCode;
import life.majiang.community.utils.VerificationCodeFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * @Date: 2022/09/12 02:18
 * @Author: mykko
 */

@Service
public class UtilService {


    private static final long TIME_OUT = 30000L;

    private VerificationCodeFactory vcf;


    private CharsetEncoder ios8859_1Encoder;

    {
        ios8859_1Encoder = Charset.forName("ISO-8859-1").newEncoder();
        int line = 1;
        int oval = 0;
        // 验证码生成工厂，包含了一些不太容易误认的字符
        vcf = new VerificationCodeFactory(45, line, oval, 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm',
                'n', 'p', 'q', 'r', 's', 't', 'w', 'x', 'y', 'z', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B',
                'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'W', 'X', 'Y', 'Z');

    }


    /**
     * @param request
     * @param response
     * @param session
     */
    public VerificationCode getNewVerCode(int length) throws IOException {
        VerificationCode vc = vcf.next(length);
        return vc;

    }
}
