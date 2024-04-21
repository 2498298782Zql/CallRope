package zql.CallRope.core.adaptor;

import java.util.HashMap;
import java.util.Map;

public abstract class ClassAdaptor {
    public static Map<String,ClassAdaptor> supportClassMap;

    public static void init(){
        supportClassMap = new HashMap<>();
        supportClassMap.put("zql/CallRope/demo/test01", new MethodAdaptorImpl());
    }

    public abstract byte[] modifyClass(String className, byte[] classBytes, String spyJarPath);
}
