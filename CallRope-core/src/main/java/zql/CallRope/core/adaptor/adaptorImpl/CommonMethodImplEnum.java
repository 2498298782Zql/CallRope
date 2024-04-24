package zql.CallRope.core.adaptor.adaptorImpl;

import javassist.*;
import zql.CallRope.core.adaptor.ClassAdaptor;

import static zql.CallRope.core.util.JavassistUtils.*;

public enum CommonMethodImplEnum implements ClassAdaptor {
    MethodAdaptorImpl{
        @Override
        public byte[] modifyClass(String className, byte[] classfileBuffer, String spyJarPath){
            try {
                ClassPool classPool = ClassPool.getDefault();
                classPool.appendClassPath(spyJarPath);
                String clazzName = className.replace("/", ".");
                CtClass ctClass = classPool.get(clazzName);
                for (CtBehavior ctBehavior : ctClass.getDeclaredMethods()) {
                    addMethodAspect(clazzName, ctBehavior, false);
                }
                ctClass.writeFile("/usr/local/CallRope");
                ctClass.detach(); // 将 CtClass 从 ClassPool 中分离的操作,以释放对类的引用，避免内存泄漏。
                return ctClass.toBytecode();
            } catch (Exception e) {
                e.printStackTrace();
                return classfileBuffer;
            }
        }
        private void addMethodAspect(String clazzName, CtBehavior ctBehavior,
                                     boolean isConstructor) throws CannotCompileException, NotFoundException {
            if (isNative(ctBehavior)
                    || isAbstract(ctBehavior)
                    || "toString".equals(ctBehavior.getName())
                    || "getClass".equals(ctBehavior.getName())
                    || "equals".equals(ctBehavior.getName())
                    || "hashCode".equals(ctBehavior.getName())) {
                return;
            }
            String methodName = isConstructor ? ctBehavior.getName() + "#" : ctBehavior.getName();
            String methodInfo = methodName + "|" + ctBehavior.getMethodInfo().getDescriptor();
            String target = isStatic(ctBehavior) ? "null" : "this";
            ctBehavior.insertBefore(
                    String.format("{zql.CallRope.point.SpyAPI.atEnter(%s, \"%s\", %s, %s);}",
                            "$class", methodInfo, target, "($w)$args")
            );
            ctBehavior.insertAfter(
                    String.format("{zql.CallRope.point.SpyAPI.atExit(%s, \"%s\", %s, %s, %s);}",
                            "$class", methodInfo, target, "($w)$args", "($w)$_")
            );
            ctBehavior.addCatch(
                    String.format("{zql.CallRope.point.SpyAPI.atExceptionExit(%s, \"%s\", %s, %s, %s);"
                                    + "throw $e;}",
                            "$class", methodInfo, target, "($w)$args", "$e"), ClassPool.getDefault().get("java.lang.Throwable"));
        }
    };

}
