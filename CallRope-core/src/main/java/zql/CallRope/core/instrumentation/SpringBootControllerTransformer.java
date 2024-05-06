package zql.CallRope.core.instrumentation;

import javassist.*;
import javassist.bytecode.SignatureAttribute;
import zql.CallRope.core.util.JavassistUtils;

import java.io.IOException;

public class SpringBootControllerTransformer implements transformer {
    private static final String USER_HOME = System.getProperty("user.home");
    private static final String CALLROPE_SPY_JAR = USER_HOME + "/CallRope/callRope-spy.jar";
    public static String controller_package = "zql.CallRope.springBootDemo.controller";
    public static final String RESTCONTROLLER_ANNOTATION = "org.springframework.web.bind.annotation.RestController";
    public static final String CONTROLLER_ANNOTATION = "org.springframework.stereotype.Controller";
    public static final String REQUESTMAPPING_ANNOTATION = "org.springframework.web.bind.annotation.RequestMapping";
    public static final String GETMAPPING_ANNOTATION = "org.springframework.web.bind.annotation.GetMapping";
    public static final String POSTTMAPPING_ANNOTATION = "org.springframework.web.bind.annotation.PostMapping";
    public static final String DELETEMAPPING_ANNOTATION = "org.springframework.web.bind.annotation.DeleteMapping";



    @Override
    public void doTransform(ClassInfo classInfo) {
        if (!classInfo.getClassName().startsWith(controller_package)) {
            return;
        }
        try {
            System.out.println("-----------------------------------------------");
            System.out.println(classInfo.getClassName());
            CtClass ctClass = classInfo.getCtClass();
            String serviceName = ctClass.getName();
            if (!ctClass.hasAnnotation(RESTCONTROLLER_ANNOTATION) && !ctClass.hasAnnotation(CONTROLLER_ANNOTATION)) {
                return;
            }
            System.out.println("2 : " + classInfo.getClassName());
            CtMethod[] declaredMethods = ctClass.getDeclaredMethods();
            for (CtMethod ctMethod : declaredMethods) {
                System.out.println(ctMethod + "==================");
                if (ctMethod.hasAnnotation(REQUESTMAPPING_ANNOTATION)
                        || ctMethod.hasAnnotation(GETMAPPING_ANNOTATION)
                        || ctMethod.hasAnnotation(POSTTMAPPING_ANNOTATION)
                        || ctMethod.hasAnnotation(DELETEMAPPING_ANNOTATION)) {
                    ClassPool classPool = ClassPool.getDefault();
                    CtClass request = classPool.get("javax.servlet.http.HttpServletRequest");
                    ctMethod.insertParameter(request);
                    String methodName = ctMethod.getName();
                    System.out.println("111111111 : " + classInfo.getClassName());
                    StringBuilder codeBefore = new StringBuilder();
                    codeBefore.append("\n");
                    codeBefore.append("javax.servlet.http.HttpServletRequest requestDuplicate= $1;\n");
                    codeBefore.append("String traceId = (String)requestDuplicate.getAttribute(\"CallRope-TraceId\");\n");
                    codeBefore.append("String pSpanId = (String)requestDuplicate.getAttribute(\"CallRope-pSpanId\");\n");
                    codeBefore.append("String spanId = (String)requestDuplicate.getAttribute(\"CallRope-spanId\");\n");
                    codeBefore.append(String.format("zql.CallRope.point.model.Span span = new zql.CallRope.point.model.SpanBuilder(traceId,spanId,pSpanId,\"%s\",\"%s\").build();\n", serviceName, methodName).toString());
                    codeBefore.append("zql.CallRope.point.SpyAPI.atFrameworkEnter(span, null);\n");
                    codeBefore.append("zql.CallRope.point.Trace.spanTtl.set(span);");
                    codeBefore.append("\n");
                    ctMethod.insertBefore(String.valueOf(codeBefore));

                    StringBuilder codeAfter = new StringBuilder();
                    codeAfter.append("\n");
                    codeAfter.append("zql.CallRope.point.model.Span spanDupilicate = (zql.CallRope.point.model.Span)zql.CallRope.point.Trace.spanTtl.get();\n");
                    codeAfter.append("zql.CallRope.point.SpyAPI.atFrameworkExit(spanDupilicate, null);");
                    codeAfter.append("\n");
                    ctMethod.insertAfter(String.valueOf(codeAfter));
                    classInfo.flag = true;
                    classInfo.setModified();
                }
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
