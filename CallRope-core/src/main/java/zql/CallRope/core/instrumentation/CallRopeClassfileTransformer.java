package zql.CallRope.core.instrumentation;

import javassist.*;
import zql.CallRope.core.adaptor.ClassAdaptor;
import zql.CallRope.core.aspect.SpyImpl;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.nio.file.Path;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import static zql.CallRope.core.util.JavassistUtils.getCtClass;
import static zql.CallRope.core.util.JavassistUtils.isClassUnderPackage;

public class CallRopeClassfileTransformer implements ClassFileTransformer {

    private final String SPY_JAR_PATH;
    private static final byte[] NO_TRANSFORM = null;
    private static final List<transformer> transformerList = new ArrayList<>();

    public CallRopeClassfileTransformer(String spyJarPath) {
        this.SPY_JAR_PATH = spyJarPath;
        SpyImpl.init();
        ClassAdaptor.init();
    }

    static {
        transformerList.add(new SpringBootHandlerInteceptorTransformer());
        transformerList.add(new SpringBootControllerTransformer());
        transformerList.add(new JdkExecutorTtlTransformlet());
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            if (className == null) return NO_TRANSFORM;
            ClassInfo classInfo = new ClassInfo(className, classfileBuffer, loader, protectionDomain);
            if (isClassUnderPackage(classInfo.getClassName(), "java.lang")) return NO_TRANSFORM;
            for (transformer transformer : transformerList) {
                transformer.doTransform(classInfo);
                if (classInfo.isModified()) {
                    return classInfo.getCtClass().toBytecode();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch (CannotCompileException e) {
            e.printStackTrace();
        }

        return NO_TRANSFORM;
    }
}
