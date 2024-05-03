package zql.CallRope.core.instrumentation;

import javassist.*;

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

            CtClass ctClass = classInfo.getCtClass();
            String serviceName = ctClass.getName();
            if (!ctClass.hasAnnotation(RESTCONTROLLER_ANNOTATION) && !ctClass.hasAnnotation(CONTROLLER_ANNOTATION)) {
                return;
            }
            CtMethod[] declaredMethods = ctClass.getDeclaredMethods();
            for (CtMethod ctMethod : declaredMethods) {
                if (ctMethod.hasAnnotation(REQUESTMAPPING_ANNOTATION)
                        || ctMethod.hasAnnotation(GETMAPPING_ANNOTATION)
                        || ctMethod.hasAnnotation(POSTTMAPPING_ANNOTATION)
                        || ctMethod.hasAnnotation(DELETEMAPPING_ANNOTATION)) {
                    ClassPool classPool = ClassPool.getDefault();
                    CtClass request = classPool.get("javax.servlet.http.HttpServletRequest");
                    ctMethod.insertParameter(request);
                    String methodName = ctMethod.getName();

                    StringBuilder codeBefore = new StringBuilder();
                    codeBefore.append("\n");
                    codeBefore.append("javax.servlet.http.HttpServletRequest requestDuplicate= $1;\n");
                    codeBefore.append("String traceID = (String)requestDuplicate.getAttribute(\"CallRope-TraceId\");\n");
                    codeBefore.append("String pSpanId = (String)requestDuplicate.getAttribute(\"CallRope-pSpanId\");\n");
                    codeBefore.append("String spanId = (String)requestDuplicate.getAttribute(\"CallRope-spanId\");\n");
                    codeBefore.append("zql.CallRope.point.model.Span span = zql.CallRope.point.SpyAPI.atFrameworkEnter(traceID,spanId,pSpanId,\"" + serviceName + "\",\"" + methodName + "\",null);");
                    codeBefore.append("zql.CallRope.point.TransmittableThreadLocal<zql.CallRope.point.model.Span> threadlocalSpan = new zql.CallRope.point.TransmittableThreadLocal<zql.CallRope.point.model.Span>();\n");
                    codeBefore.append("threadlocalSpan.set(span);\n");
                    codeBefore.append("\n");
                    ctMethod.insertBefore(String.valueOf(codeBefore));

                    StringBuilder codeAfter = new StringBuilder();
                    codeAfter.append("\n");
                    codeAfter.append("javax.servlet.http.HttpServletRequest requestDuplicate= $1;\n");
                    codeAfter.append("String traceID = (String)requestDuplicate.getAttribute(\"CallRope-TraceId\");\n");
                    codeAfter.append("String pSpanId = (String)requestDuplicate.getAttribute(\"CallRope-pSpanId\");\n");
                    codeAfter.append("String spanId = (String)requestDuplicate.getAttribute(\"CallRope-spanId\");\n");
                    codeAfter.append("zql.CallRope.point.model.Span spanDupilicate = (zql.CallRope.point.model.Span)threadlocalSpan.get();\n");
                    codeAfter.append("zql.CallRope.point.SpyAPI.atFrameworkExit(spanDupilicate, null);");
                    codeAfter.append("\n");
                    ctMethod.insertAfter(String.valueOf(codeAfter));

                    ctClass.toClass(classInfo.getClassLoader(), classInfo.getProtectionDomain());
                    classInfo.setModified();
                    return;
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
