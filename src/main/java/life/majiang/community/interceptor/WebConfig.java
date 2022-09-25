package life.majiang.community.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
//@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {


    @Autowired
    private SessionInterceptor sessionInterceptor;

    @Autowired
    private AuthorizeInterceptor authorizeInterceptor;


    @Value("${file.mapPath}")
    private String mapPath;
    @Value("${file.uploadPath}")
    private String uploadPath;

    // springMVC中的方法
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 相当于把后面位置那部分起个别名，外部可以通过别名直接访问，但是直接访问上传路径是不行的
        // registry.addResourceHandler(访问的路径).addResourceLocations(上传的路径);
        // 这个mapPath在本地测试的时候可以用到
        registry.addResourceHandler(mapPath).addResourceLocations("file:"+uploadPath);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/logout");
        registry.addInterceptor(authorizeInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/index")
                .excludePathPatterns("/authorize/*")
                .excludePathPatterns("/css/*")
                .excludePathPatterns("/fonts/*")
                .excludePathPatterns("/images/*")
                .excludePathPatterns("/js/*")
                .excludePathPatterns("/uploaddir/*")
                .excludePathPatterns("/login")
                .excludePathPatterns("/register")
                .excludePathPatterns("/error");
    }


}
