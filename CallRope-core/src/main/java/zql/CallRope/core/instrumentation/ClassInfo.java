package zql.CallRope.core.instrumentation;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import zql.CallRope.spi.annotation.SPIAuto;

import java.io.IOException;
import java.security.ProtectionDomain;

import static zql.CallRope.core.config.Configuration.getProperty;

public class ClassInfo {
    private final String transformerClassFile;
    private final String className;
    private final byte[] classFileBuffer;
    private final ClassLoader classLoader;
    private ProtectionDomain protectionDomain;
    private CtClass ctClass;
    public static String CALLROPE_SPY_JAR;

    public boolean flag = false;

    static {
        CALLROPE_SPY_JAR = getProperty("call_rope_spy_jar");
    }

    public ClassInfo(String transformerClassFile, byte[] classFileBuffer, ClassLoader classLoader, ProtectionDomain protectionDomain) {
        this.transformerClassFile = transformerClassFile;
        this.className = toClassName(transformerClassFile);
        this.classFileBuffer = classFileBuffer;
        this.classLoader = classLoader;
        this.protectionDomain = protectionDomain;
    }

    private static String toClassName(final String classFile) {
        return classFile.replace('/', '.');
    }

    private boolean modified = false;
    public boolean isModified() {
        return modified;
    }
    public void setModified() {
        this.modified = true;
    }

    public String getClassName() {
        return className;
    }

    public byte[] getClassFileBuffer() {
        return classFileBuffer;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public ProtectionDomain getProtectionDomain() {
        return protectionDomain;
    }

    public CtClass getCtClass() throws IOException {
        if(ctClass != null) return ctClass;
        try {
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(CALLROPE_SPY_JAR);
            if (this.classLoader == null) {
                classPool.appendClassPath(new LoaderClassPath(ClassLoader.getSystemClassLoader()));
            } else {
                classPool.appendClassPath(new LoaderClassPath(this.classLoader));
            }
            CtClass ctClass = classPool.get(className);
            this.ctClass = ctClass;
            if (ctClass != null) {
                return ctClass;
            } else {
                return null;
            }
        } catch (NotFoundException e) {
            System.out.println(this.classLoader);
            e.printStackTrace();
        }
        return null;
    }

}
