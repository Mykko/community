//package life.majiang.community.advice;
//
//import com.google.gson.Gson;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * @Date: 2022/09/12 15:14
// * @Author: mykko
// */
//
//@Aspect
//@Component
//public class Logutil {
//
//    private final static Logger logger = LoggerFactory.getLogger(Logutil.class);
//
//
////    @Pointcut("execution(public * com.mykko.communitylearn.controller.*.*(..)) && !execution(public * com.mykko.communitylearn.controller.UtilController.*(..))")
//    @Pointcut("execution(public * life.majiang.community.controller.*.*(..))")
//    public void webLog() {}
//
//    @Pointcut("execution(public * life.majiang.community.*..*(..))")
//    public void errorLog() {}
//
//
//    /**
//     * 环绕
//     * @param proceedingJoinPoint
//     * @return
//     * @throws Throwable
//     */
//    @Around("webLog()")
//    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//
//        // 开始打印请求日志
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//
//        // 打印请求相关参数
//        logger.info("");
//        logger.info("========================================== Start ==========================================");
//        // 打印请求 url
//        logger.info("URL            : {}", request.getRequestURL().toString());
//        // 打印 Http method
//        logger.info("HTTP Method    : {}", request.getMethod());
//        // 打印调用 controller 的全路径以及执行方法
//        logger.info("Class Method   : {}.{}", proceedingJoinPoint.getSignature().getDeclaringTypeName(), proceedingJoinPoint.getSignature().getName());
//        // 打印请求的 IP
//        logger.info("IP             : {}", request.getRemoteAddr());
//        // 打印请求入参
//        logger.info("Request Args   : {}", new Gson().toJson(request.getParameterMap()));
//
//
//        long startTime = System.currentTimeMillis();
//        Object result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
//        // 打印出参
//        logger.info("Response Args  : {}", new Gson().toJson(result));
//        // 执行耗时
//        logger.info("Time-Consuming : {} ms", System.currentTimeMillis() - startTime);
//
//        logger.info("=========================================== End ===========================================");
//        // 每个请求之间空一行
//        logger.info("");
//        return result;
//    }
//
//    @AfterThrowing(value = "errorLog()", throwing = "t")
//    public void afterThrowing(Throwable t){
//        logger.error(t.getMessage(), t);
//
//    }
//
//}
