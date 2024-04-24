package zql.CallRope.springBootDemo.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Component
public class myfilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println(myfilter.class.getClassLoader());
        
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String visitIp = req.getRemoteAddr();
        visitIp = "0:0:0:0:0:0:0:1".equals(visitIp) ? "127.0.0.1" : visitIp;
        System.out.println("访问 IP = " + visitIp);
        filterChain.doFilter(req, servletResponse);
    }
}
