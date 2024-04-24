package zql.CallRope.springBootDemo.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println(UserLoginInterceptor.class.getClassLoader().toString());
        System.out.println("开始处理");
        Thread.sleep(1000);
        return true;
    }
}
