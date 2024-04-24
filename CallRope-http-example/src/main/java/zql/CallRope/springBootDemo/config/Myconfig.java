package zql.CallRope.springBootDemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import zql.CallRope.springBootDemo.handler.UserLoginInterceptor;

@Configuration
public class Myconfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(new UserLoginInterceptor()).order(1);
    }
}
