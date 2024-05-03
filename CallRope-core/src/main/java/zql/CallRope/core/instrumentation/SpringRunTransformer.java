package zql.CallRope.core.instrumentation;

import javassist.*;

import java.io.IOException;

public class SpringRunTransformer implements transformer {
    @Override
    public void doTransform(ClassInfo classInfo) {
        if (!classInfo.getClassName().contains("controller")) {
            return;
        }
        try {
            CtClass ctClass = classInfo.getCtClass();
            CtClass[] declaredClasses = ctClass.getDeclaredClasses();
            ClassPool classPool2 = ClassPool.getDefault();
            if (ctClass.subtypeOf(classPool2.get("java.lang.Runnable"))) {
                CtMethod run = ctClass.getDeclaredMethod("run");
                run.insertBefore("System.out.println(threadlocalSpan.get());");
            }
            classInfo.setModified();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }
}
