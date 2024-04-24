package zql.CallRope.core.aspect.aspectImpl;

import javassist.*;
import zql.CallRope.core.aspect.FrameworkAspect;

import java.util.Map;

public class SpringHandlerAspectImpl implements FrameworkAspect {
    @Override
    public void entry(String traceId, String spanId, String parentSpanId, Map<String, Object> infos) {
        // do nothing
    }

    @Override
    public void exit(String traceId, String spanId, String parentSpanId, Map<String, Object> infos) {
        try {
            System.out.println("borrowLoader");
            ClassLoader loader = (ClassLoader) infos.get("loader");
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(loader));
            CtClass ctClass = classPool.getCtClass("zql.CallRope.springBootDemo.handler.UserLoginInterceptor");
            CtMethod ctMeThodImpl = ctClass.getDeclaredMethod("preHandle");
            System.out.println("UserLoginInterceptor不为空");
            ctMeThodImpl.insertAfter("System.out.println(\"我是handlerImpl\");");
            Class<?> clazz2 = ctClass.toClass(loader, ctClass.getClass().getProtectionDomain());
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }

    }
}
