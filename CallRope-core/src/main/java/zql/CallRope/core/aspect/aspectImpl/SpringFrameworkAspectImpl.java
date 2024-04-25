package zql.CallRope.core.aspect.aspectImpl;

import javassist.*;
import zql.CallRope.core.aspect.FrameworkAspect;

import java.util.Map;

public class SpringFrameworkAspectImpl implements FrameworkAspect {
    @Override
    public void entry(String traceId, String spanId, String parentSpanId, Map<String, Object> infos) {
        System.out.println("entry");
    }

    @Override
    public void exit(String traceId, String spanId, String parentSpanId, Map<String, Object> infos) {
        try {
            ClassLoader loader = (ClassLoader) infos.get("loader");
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(loader));
            CtClass ctClass = classPool.getCtClass("zql.CallRope.springBootDemo.controller.loginContoller");
            CtMethod ctMeThodImpl = ctClass.getDeclaredMethod("login");
            ctMeThodImpl.insertAfter("System.out.println(\"i am login\");");
            ctClass.toClass(loader, ctClass.getClass().getProtectionDomain());
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }
}
