package zql.CallRope.core.instrumentation;

import javassist.*;
import zql.CallRope.core.adaptor.ClassAdaptor;
import zql.CallRope.core.adaptor.adaptorImpl.HttpMethodImplEnum;
import zql.CallRope.core.aspect.SpyImpl;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.MessageDigest;
import java.security.ProtectionDomain;
import static zql.CallRope.core.adaptor.ClassAdaptor.supportClassMap;

public class CallRopeClassfileTransformer implements ClassFileTransformer {

    private final String SPY_JAR_PATH;

    public CallRopeClassfileTransformer(String spyJarPath) {
        this.SPY_JAR_PATH = spyJarPath;
        SpyImpl.init();
        ClassAdaptor.init();
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try{
            if(className.contains("zql/CallRope/springBootDemo/controller")){
                System.out.println(loader);
                System.out.println(className);
                ClassPool classPool = ClassPool.getDefault();
                classPool.appendClassPath(new LoaderClassPath(loader));
                CtClass ctClass = classPool.get("zql.CallRope.springBootDemo.controller.loginController");
                CtMethod[] declaredMethods = ctClass.getDeclaredMethods();
                for(CtMethod ctMethod : declaredMethods){
                    ctMethod.insertAfter("System.out.println(\"12345\");");
                }
                ctClass.toClass(loader,ctClass.getClass().getProtectionDomain());
                return null;
            }
        }catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        return classfileBuffer;
    }
}
