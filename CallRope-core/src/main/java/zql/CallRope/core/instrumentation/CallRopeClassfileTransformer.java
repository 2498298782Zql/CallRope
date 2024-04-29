package zql.CallRope.core.instrumentation;

import javassist.*;
import zql.CallRope.core.adaptor.ClassAdaptor;
import zql.CallRope.core.adaptor.adaptorImpl.HttpMethodImplEnum;
import zql.CallRope.core.aspect.SpyImpl;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.MessageDigest;
import java.security.ProtectionDomain;
import static zql.CallRope.core.adaptor.ClassAdaptor.supportClassMap;

public class CallRopeClassfileTransformer implements ClassFileTransformer {

    private final String SPY_JAR_PATH;
    private static final byte[] NO_TRANSFORM = null;


    public CallRopeClassfileTransformer(String spyJarPath) {
        this.SPY_JAR_PATH = spyJarPath;
        SpyImpl.init();
        ClassAdaptor.init();
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {




        return NO_TRANSFORM;
    }
}
