package zql.CallRope.core.instrumentation;

public class ClassInfo {
    private final String transformerClassFile;
    private final String className;
    private final byte[] classFileBuffer;
    private final ClassLoader classLoader;

    public ClassInfo(String transformerClassFile, byte[] classFileBuffer, ClassLoader classLoader) {
        this.transformerClassFile = transformerClassFile;
        this.className = toClassName(transformerClassFile);
        this.classFileBuffer = classFileBuffer;
        this.classLoader = classLoader;
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
}
