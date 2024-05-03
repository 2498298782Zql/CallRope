package zql.CallRope.core.adaptor;

import zql.CallRope.core.adaptor.adaptorImpl.CommonMethodImplEnum;
import zql.CallRope.core.adaptor.adaptorImpl.HttpMethodImplEnum;
import zql.CallRope.core.instrumentation.ClassInfo;


import java.util.HashMap;
import java.util.Map;

public interface ClassAdaptor {
    Map<String,ClassAdaptor> supportClassMap = new HashMap<>(); // public static final

    static void init(){
        // TODO 阅读配置类
        supportClassMap.put("zql/CallRope/demo/test01", CommonMethodImplEnum.MethodAdaptorImpl);
    }
    public byte[] modifyClass(ClassInfo classInfo, String spyJarPath);
}
