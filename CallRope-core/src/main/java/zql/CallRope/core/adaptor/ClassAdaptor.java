package zql.CallRope.core.adaptor;

import zql.CallRope.core.adaptor.adaptorImpl.MethodAdaptorImpl;
import zql.CallRope.core.adaptor.adaptorImpl.SpringBootAdaptorImpl;

import java.util.HashMap;
import java.util.Map;

public abstract class ClassAdaptor {
    public static Map<String,ClassAdaptor> supportClassMap;

    public static void init(){
        supportClassMap = new HashMap<>();
        // TODO 阅读配置类
        supportClassMap.put("zql/CallRope/demo/test01", new MethodAdaptorImpl());
        supportClassMap.put("zql/CallRope/springBootDemo/testmain", new SpringBootAdaptorImpl());
    }

    public abstract byte[] modifyClass(String className, byte[] classfileBuffer, String spyJarPath);
}
