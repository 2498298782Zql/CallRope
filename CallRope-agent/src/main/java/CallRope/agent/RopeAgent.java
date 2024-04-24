package CallRope.agent;

import CallRope.agent.classloader.CallRopeClassLoader;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.jar.JarFile;

@Slf4j
public class RopeAgent {
    private static final String USER_HOME = System.getProperty("user.home");
    private static final String CALLROPE_SPY_JAR = USER_HOME + "/CallRope/callRope-spy.jar";
    private static final String CALLROPE_CORE_JAR = USER_HOME + "/CallRope/callRope-core.jar";
    private static ClassLoader callRopeClassLoader;
    private static final String TRANSFORMER ="zql.CallRope.core.instrumentation.CallRopeClassfileTransformer";

    private static ClassLoader getClassLoader(File agentCoreFile) throws Throwable {
        return loadOrDefineClassLoader(agentCoreFile);
    }

    private static ClassLoader loadOrDefineClassLoader(File agentCoreFile) throws Throwable {
        if (callRopeClassLoader == null) {
            callRopeClassLoader = new CallRopeClassLoader(new URL[] {agentCoreFile.toURI().toURL()});
        }
        return callRopeClassLoader;
    }

    public static void premain(String agentOps, Instrumentation instrumentation){
        main(agentOps, instrumentation);
    }
    public static void main(String agentOps,Instrumentation instrumentation){
        try {
            System.out.println("获取到的参数" + agentOps);
            // 添加监视包路径
            log.info("CallRope start...");
            File agentSpyFile =new File(CALLROPE_SPY_JAR);
            if(!agentSpyFile.exists()){
                log.info("Spy jar file does not exist: " + CALLROPE_SPY_JAR);
                return;
            }
            // 使用自定义加载器加载 CALLROPE_CORE_JAR,防止污染线上的代码
            instrumentation.appendToBootstrapClassLoaderSearch(new JarFile(agentSpyFile));
            File agentCoreFile =new File(CALLROPE_CORE_JAR);
            if(!agentCoreFile.exists()){
                log.info("Agent-core jar file does not exist:" + CALLROPE_CORE_JAR);
                return;
            }
            ClassLoader callRopeClassLoader = getClassLoader(agentCoreFile);
            Class<?> CallRopeClassFileTransform = callRopeClassLoader.loadClass(TRANSFORMER);
            Constructor<?> transform = CallRopeClassFileTransform.getDeclaredConstructor(String.class);
            instrumentation.addTransformer((ClassFileTransformer) transform.newInstance(CALLROPE_SPY_JAR));
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
    }
}
