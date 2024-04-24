package zql.CallRope.core.adaptor.adaptorImpl;


import javassist.*;
import zql.CallRope.core.adaptor.ClassAdaptor;

public enum HttpMethodImplEnum implements ClassAdaptor{
    SpringBootAdaptorImpl{ // 适配springboot
        @Override
        public byte[] modifyClass(String className, byte[] classfileBuffer, String spyJarPath) {
            try {
                ClassPool classPool = ClassPool.getDefault();
                classPool.appendClassPath(spyJarPath);
                CtClass ctClass = classPool.get("org.springframework.boot.loader.LaunchedURLClassLoader");
                CtConstructor[] ctConstructors = ctClass.getDeclaredConstructors();
                if(ctConstructors.length > 0){
                    System.out.println("ctConstructors大于0");
                    Thread.sleep(3000);
                    System.out.println("睡觉结束 ：" + ctConstructors.length);
                }
                System.out.println("ctConstructors大于0睡觉结束 ：" + ctConstructors.length);
                for(CtConstructor ctConstructor: ctConstructors){
                    System.out.println("插入中1" + ctConstructor);
                    ctConstructor.insertAfter("System.out.println(\"borrow method called\");");
                    Thread.sleep(10000);
                    System.out.println("插入中2" + ctConstructor);
                    ctConstructor.insertAfter("System.out.println(\"borrowLoader method called\");");
                    System.out.println("插入中3" + ctConstructor);
                    ctConstructor.insertAfter("zql.CallRope.core.adaptor.adaptorImpl.HttpMethodImplEnum.testInsert(1)");
                    Thread.sleep(10000);
                }
                System.out.println("for结束");
                Thread.sleep(5000);
                ctClass.writeFile("/usr/local/CallRope");
                return ctClass.toBytecode();
            } catch (Exception e) {
                System.out.println("可恶啊");
                System.out.println(e);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }

                return classfileBuffer;
            }

        }
    };
    public static void testInsert(int i){
        System.out.println("测试添加" + i);
    }

    public static void borrowLoader(ClassLoader loader) {
        try {
            Thread.sleep(10000);
            System.out.println("borrowLoader");
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(loader));
            CtClass ctClass = classPool.getCtClass("org.springframework.web.servlet.HandlerInterceptor");
            System.out.println("HandlerInterceptor不为空");
            Thread.sleep(5000);
            CtMethod ctMethod = ctClass.getDeclaredMethod("prehandle");
            ctMethod.insertAfter("System.out.println(\"我是handler\");");
            CtClass ctClass2 = classPool.getCtClass("zql.CallRope.springBootDemo.handler.UserLoginInterceptor");
            CtMethod ctMeThodImpl = ctClass.getDeclaredMethod("prehandle");
            System.out.println("UserLoginInterceptor不为空");
            Thread.sleep(5000);
            ctMeThodImpl.insertAfter("System.out.println(\"我是handlerImpl\");");
            Class<?> clazz = ctClass.toClass(loader, ctClass.getClass().getProtectionDomain());
            Class<?> clazz2 = ctClass2.toClass(loader, ctClass.getClass().getProtectionDomain());
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void borrow(ClassLoader loader){
        try {
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(loader));
            CtClass ctClass = classPool.get("org.springframework.web.servlet.config.annotation.InterceptorRegistry");
            CtMethod ctMethod = ctClass.getDeclaredMethod("getInterceptors");
            ctMethod.insertBefore("i hava been success");
            Thread.sleep(5000);
            ctClass.toClass(loader, ctClass.getClass().getProtectionDomain());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
