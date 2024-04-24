package zql.CallRope.core.adaptor;

import zql.CallRope.core.adaptor.adaptorImpl.CommonMethodImplEnum;
import zql.CallRope.core.adaptor.adaptorImpl.HttpMethodImplEnum;


import java.util.HashMap;
import java.util.Map;

public interface ClassAdaptor {
    Map<String,ClassAdaptor> supportClassMap = new HashMap<>(); // public static final

    static void init(){
        // TODO 阅读配置类
        supportClassMap.put("zql/CallRope/demo/test01", CommonMethodImplEnum.MethodAdaptorImpl);
        supportClassMap.put("zql/CallRope/springBootDemo/handler/UserLoginInterceptor", HttpMethodImplEnum.SpringBootAdaptorImpl);
        supportClassMap.put("org.springframework.web.bind.annotation.RestController",HttpMethodImplEnum.SpringBootAdaptorImpl);
    }
    public byte[] modifyClass(String className, byte[] classfileBuffer, String spyJarPath);
}
