package zql.CallRope.core.instrumentation;

import javassist.*;
import zql.CallRope.core.aspect.SpyImpl;
import zql.CallRope.core.instrumentation.dubbo.DubboConsumerFilterTransformer;
import zql.CallRope.core.instrumentation.dubbo.DubboProducerFilterTransformer;
import zql.CallRope.spi.SpiBs;
import zql.CallRope.spi.api.IExtensionLoader;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import static zql.CallRope.core.util.JavassistUtils.isClassUnderPackage;

public class CallRopeClassfileTransformer implements ClassFileTransformer {

    private final String SPY_JAR_PATH;
    private static final byte[] NO_TRANSFORM = null;
    private static final List<transformer> transformerList = new ArrayList<>();

    public CallRopeClassfileTransformer(String spyJarPath) {
        this.SPY_JAR_PATH = spyJarPath;
        SpyImpl.init();
    }

    static {
        init();
    }

    public static void init(){
        IExtensionLoader<transformer> load = SpiBs.load(transformer.class, transformer.class.getClassLoader());
        List<transformer> allExtension = load.getAllExtension();
        for(transformer transformer: allExtension){
            transformerList.add(transformer);
        }
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            if (className == null) return classfileBuffer;
            ClassInfo classInfo = new ClassInfo(className, classfileBuffer, loader, protectionDomain);
            if (isClassUnderPackage(classInfo.getClassName(), "java.lang")) return NO_TRANSFORM;
            // 责任链
            for (transformer transformer : transformerList) {
                transformer.doTransform(classInfo);
                if (classInfo.isModified()) {
                    if(classInfo.getClassLoader() == null || classInfo.flag){
                        return classInfo.getCtClass().toBytecode();
                    }
                    return NO_TRANSFORM;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return classfileBuffer;
    }


    // 技术验证
    public static void main(String[] args) {
        for(transformer transformer : transformerList){
            System.out.println(transformer);
        }
        System.out.println("输出结束");
    }

}
