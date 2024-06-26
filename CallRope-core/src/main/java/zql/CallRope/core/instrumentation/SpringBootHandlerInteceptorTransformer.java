package zql.CallRope.core.instrumentation;

import javassist.*;

import java.io.IOException;

import static zql.CallRope.core.config.Configuration.getProperty;

public class SpringBootHandlerInteceptorTransformer implements transformer {
    public static final String HANDLER_INTECEPTOR;


    static {
        HANDLER_INTECEPTOR = getProperty("enhance_interceptor_class");
    }

    @Override
    public void doTransform(ClassInfo classInfo) {
        if (!classInfo.getClassName().equals(HANDLER_INTECEPTOR)) {
            return;
        }
        try {
            CtClass ctClass = classInfo.getCtClass();
            CtMethod preHandle = ctClass.getDeclaredMethod("preHandle");
            StringBuilder code = new StringBuilder();
            code.append("{\n");
            code.append("javax.servlet.http.HttpServletRequest requestDuplicate = $1;\n");
            code.append("String traceId = requestDuplicate.getHeader(\"TraceId\");\n");
            code.append("if (traceId == null || traceId.isEmpty()) {\n");
            code.append("    traceId = zql.CallRope.point.IDutils.TraceIdGenerator.generateTraceId();\n");
            code.append("    requestDuplicate.setAttribute(\"CallRope-TraceId\", traceId);\n");
            code.append("}\n");
            code.append("}\n");
            preHandle.insertBefore(String.valueOf(code));
            if(classInfo.getClassLoader() != null){
                ctClass.toClass(classInfo.getClassLoader(), classInfo.getProtectionDomain());
            }
            classInfo.setModified();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
