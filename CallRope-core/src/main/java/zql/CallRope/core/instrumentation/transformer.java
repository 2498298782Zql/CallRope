package zql.CallRope.core.instrumentation;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import java.io.IOException;
import java.security.ProtectionDomain;

public interface transformer {
    void doTransform(ClassInfo classInfo);
}
