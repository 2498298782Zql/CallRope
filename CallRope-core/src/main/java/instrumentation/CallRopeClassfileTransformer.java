package instrumentation;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Map;

public class CallRopeClassfileTransformer implements ClassFileTransformer {

    private final String SPY_JAR_PATH;
    // 使用策略模式优化if-else
    private final static Map<String> supportClassSet = new HashSet<>();

    static {
        supportClassSet.add("zql.CallRope.testMethod");
    }

    public CallRopeClassfileTransformer(String spyJarPath) {
        this.SPY_JAR_PATH = spyJarPath;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        if(!supportClassSet.contains(className)){
            return classfileBuffer;
        }
        







        return new byte[0];
    }
}
