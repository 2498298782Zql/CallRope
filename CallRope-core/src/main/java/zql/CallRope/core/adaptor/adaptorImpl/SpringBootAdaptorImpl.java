package zql.CallRope.core.adaptor.adaptorImpl;

import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtMethod;
import zql.CallRope.core.adaptor.ClassAdaptor;

public class SpringBootAdaptorImpl extends ClassAdaptor {
    @Override
    public byte[] modifyClass(String className, byte[] classfileBuffer, String spyJarPath) {
        
        try {
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(spyJarPath);
            String clazzName = className.replace("/", ".");
            CtClass ctClass = classPool.getCtClass(clazzName);
            CtMethod doHandlerMethod = ctClass.getDeclaredMethod("doFilter");
            doHandlerMethod.insertBefore("{" +
                    "");

            return ctClass.toBytecode();
        } catch (Exception e) {
            e.printStackTrace();
            return classfileBuffer;
        }
    }

}
