package zql.CallRope.core.adaptor.adaptorImpl;


import javassist.*;
import zql.CallRope.core.adaptor.ClassAdaptor;

import java.util.HashMap;
import java.util.Map;

public enum HttpMethodImplEnum implements ClassAdaptor {
    SpringBootAdaptorImpl { // 适配springboot
        @Override
        public byte[] modifyClass(String className, byte[] classfileBuffer, String spyJarPath) {
            try {
                String USER_HOME = System.getProperty("user.home");
                ClassPool classPool = ClassPool.getDefault();
                classPool.appendClassPath(spyJarPath);
                CtClass ctClass = classPool.get("org.springframework.boot.loader.LaunchedURLClassLoader");
                CtConstructor[] ctConstructors = ctClass.getDeclaredConstructors();
                if (ctConstructors.length > 0) {
                    System.out.println("ctConstructors大于0");
                    System.out.println("睡觉结束 ：" + ctConstructors.length);
                }
                System.out.println("ctConstructors大于0睡觉结束 ：" + ctConstructors.length);
                System.out.println("1");
                for (CtConstructor ctConstructor : ctConstructors) {
                    ctConstructor.insertAfter(
                            "{java.util.Map/*<String, Object>*/ handlerMap = new java.util.HashMap/*<>*/();" +
                                "handlerMap.put(\"loader\",this);" +
                                String.format("zql.CallRope.point.SpyAPI.atFrameworkExit(\"%s\",\"%s\", \"%s\", %s);}", "111", "1", "123", "handlerMap")

                    );
                    Thread.sleep(10000);
                }
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

}
