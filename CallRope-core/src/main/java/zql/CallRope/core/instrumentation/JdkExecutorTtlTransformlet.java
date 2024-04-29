package zql.CallRope.core.instrumentation;

import javassist.*;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import static zql.CallRope.core.util.JavassistUtils.getCtClass;
import static zql.CallRope.core.util.JavassistUtils.isClassAtPackageJavaUtil;

public class JdkExecutorTtlTransformlet implements TtlTransformlet{
    protected static final String RUNNABLE_CLASS_NAME = "java.lang.Runnable";
    protected static final String CALLABLE_CLASS_NAME = "java.util.concurrent.Callable";

    protected static final String TTL_RUNNABLE_CLASS_NAME = "zql.CallRope.point.TtlRunnable";
    protected static final String TTL_CALLABLE_CLASS_NAME = "zql.CallRope.point.TtlCallable";

    protected final boolean disableInheritableForThreadPool; // 是否需要基于线程池的跨线程传递

    protected static final String THREAD_POOL_EXECUTOR_CLASS_NAME = "java.util.concurrent.ThreadPoolExecutor";

    private final Map<String, String> paramTypeNameToDecorateMethodClass = new HashMap<>();
    public JdkExecutorTtlTransformlet(boolean disableInheritableForThreadPool) {
        disableInheritableForThreadPool = false; // 后续再做
        this.disableInheritableForThreadPool = disableInheritableForThreadPool;
        paramTypeNameToDecorateMethodClass.put(RUNNABLE_CLASS_NAME, TTL_RUNNABLE_CLASS_NAME);
        paramTypeNameToDecorateMethodClass.put(CALLABLE_CLASS_NAME, TTL_CALLABLE_CLASS_NAME);
    }

    @Override
    public void doTransform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws CannotCompileException, NotFoundException, IOException {
        if (isClassAtPackageJavaUtil(className)) return;
        if(className.equals("java/util/concurrent/ThreadPoolExecutor")){
            CtClass ctClass = getCtClass(loader, className);
            if(ctClass == null){
                return;
            }
            for(CtMethod ctMethod : ctClass.getDeclaredMethods()){
                decorateMethodWithParameterHasRunnableOrCallable(ctMethod);
            }
            ctClass.toClass(loader,protectionDomain);
        }
    }

    private void decorateMethodWithParameterHasRunnableOrCallable(final CtMethod method) throws NotFoundException, CannotCompileException {
        final int modifiers = method.getModifiers();
        if (!java.lang.reflect.Modifier.isPublic(modifiers) || Modifier.isStatic(modifiers)) return;

        CtClass[] parameterTypes = method.getParameterTypes();
        StringBuilder insertCode = new StringBuilder();
        for (int i = 0; i < parameterTypes.length; i++) {
            final String paramTypeName = parameterTypes[i].getName();
            if (paramTypeNameToDecorateMethodClass.containsKey(paramTypeName)) {
                String code = String.format(
                        // auto decorate to TTL wrapper
                        "$%d = zql.CallRope.point.AutoWrap.doAutoWrap($%<d);",
                        i + 1);
                insertCode.append(code);
            }
        }
        if (insertCode.length() > 0) {
            method.insertBefore(insertCode.toString());
        }
    }

}
