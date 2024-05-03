package zql.CallRope.core.adaptor.adaptorImpl;


import javassist.*;
import zql.CallRope.core.adaptor.ClassAdaptor;
import zql.CallRope.core.instrumentation.ClassInfo;

import java.util.HashMap;
import java.util.Map;

public enum HttpMethodImplEnum implements ClassAdaptor {
    SpringBootControllerAdaptorImpl { // 适配springboot

        @Override
        public byte[] modifyClass(ClassInfo classInfo, String spyJarPath) {
            return new byte[0];
        }
    };

}
