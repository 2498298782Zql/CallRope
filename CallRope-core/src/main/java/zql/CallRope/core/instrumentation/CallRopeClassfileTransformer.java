package zql.CallRope.core.instrumentation;

import zql.CallRope.core.adaptor.ClassAdaptor;
import zql.CallRope.core.adaptor.adaptorImpl.HttpMethodImplEnum;
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
        if(className.contains("zql/CallRope/springBootDemo/controller")){
            System.out.println(loader);
            System.out.println(className);
        }
        if(!className.equals("org/springframework/boot/loader/LaunchedURLClassLoader")){
            return classfileBuffer;
        }
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("transform:" + className);
        System.out.println("---------------------------------------------------------------------------------------");
        if(className.equals("org/springframework/boot/loader/LaunchedURLClassLoader")){
            System.out.println(loader);
            return HttpMethodImplEnum.SpringBootAdaptorImpl.modifyClass(className,classfileBuffer,SPY_JAR_PATH);
        }
        return classfileBuffer;
    }
}
