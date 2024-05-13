package CallRope.agent;

import CallRope.agent.classloader.CallRopeClassLoader;

import java.io.File;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.jar.JarFile;

import static CallRope.agent.config.Configuration.getProperty;

public class RopeAgent {
    private static final String CALLROPE_SPY_JAR;
    private static final String CALLROPE_CORE_JAR;
    private static ClassLoader callRopeClassLoader;
    private static final String TRANSFORMER;
    private static final String threadpoolClassName = "java.util.concurrent.ThreadPoolExecutor";

    static {
        CALLROPE_SPY_JAR = getProperty("rope_spy_jar");
        CALLROPE_CORE_JAR = getProperty("rope_core_jar");
        TRANSFORMER = getProperty("rope_transform");
    }

    private static ClassLoader getClassLoader(File agentCoreFile) throws Throwable {
        return loadOrDefineClassLoader(agentCoreFile);
    }

    private static ClassLoader loadOrDefineClassLoader(File agentCoreFile) throws Throwable {
        if (callRopeClassLoader == null) {
            callRopeClassLoader = new CallRopeClassLoader(new URL[]{agentCoreFile.toURI().toURL()});
        }
        return callRopeClassLoader;
    }

    public static void premain(String agentOps, Instrumentation instrumentation) {
        main(agentOps, instrumentation);
    }

    public static void main(String agentOps, Instrumentation instrumentation) {
        try {
            // 添加监视包路径
            File agentSpyFile = new File(CALLROPE_SPY_JAR);
            if (!agentSpyFile.exists()) {
                return;
            }
            // 使用自定义加载器加载 CALLROPE_CORE_JAR,防止污染线上的代码
            instrumentation.appendToBootstrapClassLoaderSearch(new JarFile(agentSpyFile));
            File agentCoreFile = new File(CALLROPE_CORE_JAR);
            if (!agentCoreFile.exists()) {
                return;
            }
            ClassLoader callRopeClassLoader = getClassLoader(agentCoreFile);
            Class<?> CallRopeClassFileTransform = callRopeClassLoader.loadClass(TRANSFORMER);
            Constructor<?> transform = CallRopeClassFileTransform.getDeclaredConstructor(String.class);
            instrumentation.addTransformer((ClassFileTransformer) transform.newInstance(CALLROPE_SPY_JAR), true);
            Class[] allLoadedClasses = instrumentation.getAllLoadedClasses();
            Class<?> threadPoolExecutorClass = null;
            for (Class<?> clazz : allLoadedClasses) {
                if (clazz.getName().equals(threadpoolClassName)) {
                    threadPoolExecutorClass = clazz;
                }
            }
            instrumentation.retransformClasses(threadPoolExecutorClass);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
