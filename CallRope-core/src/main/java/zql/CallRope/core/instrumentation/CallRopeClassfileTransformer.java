package zql.CallRope.core.instrumentation;

import zql.CallRope.core.adaptor.ClassAdaptor;
import zql.CallRope.core.aspect.SpyImpl;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import static zql.CallRope.core.adaptor.ClassAdaptor.supportClassMap;

public class CallRopeClassfileTransformer implements ClassFileTransformer {

    private final String SPY_JAR_PATH;

    public CallRopeClassfileTransformer(String spyJarPath) {
        this.SPY_JAR_PATH = spyJarPath;
        SpyImpl.init();
        ClassAdaptor.init();
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        System.out.println("transform supportBefore:" + className);
        if(!supportClassMap.containsKey(className)){
            return classfileBuffer;
        }
        System.out.println("transform:" + className);
        ClassAdaptor adaptor = supportClassMap.get(className);
        if(adaptor != null){
            return adaptor.modifyClass(className,classfileBuffer,SPY_JAR_PATH);
        }
        return classfileBuffer;
    }
}
