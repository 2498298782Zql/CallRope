package zql.CallRope.core.instrumentation;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import zql.CallRope.spi.annotation.SPI;

import java.io.IOException;
import java.security.ProtectionDomain;

@SPI
public interface transformer {
    void doTransform(ClassInfo classInfo) throws IOException, NotFoundException, ClassNotFoundException, CannotCompileException;
}
