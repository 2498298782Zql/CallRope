package zql.CallRope.core.util;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;


// todo 责任链
public class ClassFilterChain implements ClassFileTransformer {
    private List<ClassFileTransformer> transformers = new ArrayList<>();

    public void addTransformer(ClassFileTransformer transformer) {
        transformers.add(transformer);
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (shouldBeIntercepted(className)) {
            for (ClassFileTransformer transformer : transformers) {
                byte[] transformedClass = transformer.transform(loader, className, classBeingRedefined,
                        protectionDomain, classfileBuffer);
                if (transformedClass != null) {
                    return transformedClass;
                }
            }
        }
        return null;
    }

    private boolean shouldBeIntercepted(String className) {
        // 这里你可以编写逻辑来判断是否需要拦截这个类
        return className.startsWith("com.example.controller") ||
                className.startsWith("com.example.handler") ||
                className.startsWith("java.util.concurrent"); // 例如，拦截线程池类
    }
}

