package zql.CallRope.core.instrumentation;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import java.io.IOException;
import java.security.ProtectionDomain;

public interface TtlTransformlet {
    void doTransform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                     ProtectionDomain protectionDomain, byte[] classfileBuffer) throws CannotCompileException, NotFoundException, IOException;
}
