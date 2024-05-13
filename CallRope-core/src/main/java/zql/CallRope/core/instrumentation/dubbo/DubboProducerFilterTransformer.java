package zql.CallRope.core.instrumentation.dubbo;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import zql.CallRope.core.instrumentation.ClassInfo;
import zql.CallRope.core.instrumentation.transformer;

import java.io.IOException;
import java.util.Set;

import static zql.CallRope.core.config.Configuration.getPropertyAsSet;

public class DubboProducerFilterTransformer implements transformer {
    private static Set<String> filterProviderInvoke;

    static {
        filterProviderInvoke = getPropertyAsSet("filter_producer_class");
    }

    @Override
    public void doTransform(ClassInfo classInfo) throws IOException, NotFoundException, ClassNotFoundException, CannotCompileException {
        if (!filterProviderInvoke.contains(classInfo.getClassName())) {
            return;
        }
        CtClass ctClass = classInfo.getCtClass();
        if (ctClass == null) {
            throw new ClassNotFoundException(classInfo.getClassName() + ":没有这个类");
        }
        CtMethod invoke = ctClass.getDeclaredMethod("invoke");
        if (invoke == null) {
            throw new ClassNotFoundException(classInfo.getClassName() + ":缺少invoke方法");
        }
        StringBuilder codeBefore = new StringBuilder();
        codeBefore.append("String traceId = com.alibaba.dubbo.rpc.RpcContext.getContext().getAttachment(\"rope-traceId\");\n");
        codeBefore.append("String pSpanId = com.alibaba.dubbo.rpc.RpcContext.getContext().getAttachment(\"rope-pSpanId\");");
        codeBefore.append("String spanId = com.alibaba.dubbo.rpc.RpcContext.getContext().getAttachment(\"rope-spanId\");");
        codeBefore.append("if(traceId == null || \"\".equals(traceId)){\n");
        codeBefore.append("   traceId = zql.CallRope.point.IDutils.TraceIdGenerator.generateTraceId();\n");
        codeBefore.append("}\n");
        codeBefore.append("String serviceInterfaceName = $1.getUrl().getServiceInterface();\n");
        codeBefore.append("String methodName = $2.getMethodName();\n");
        codeBefore.append("zql.CallRope.point.model.Span span = new zql.CallRope.point.model.SpanBuilder(traceId,pSpanId + \".\" +  spanId, pSpanId, serviceInterfaceName, methodName).build();\n");
        codeBefore.append("zql.CallRope.point.SpyAPI.atFrameworkEnter(span, null, new String[]{\"DubboProducerAspectImpl\"});\n");

        StringBuilder codeAfter = new StringBuilder();
        codeAfter.append("zql.CallRope.point.model.Span spanDupilicate = (zql.CallRope.point.model.Span)zql.CallRope.point.Trace.spanTtl.get();\n");
        codeAfter.append("zql.CallRope.point.SpyAPI.atFrameworkExit(spanDupilicate, null, new String[]{\"DubboProducerAspectImpl\"});\n");
        invoke.insertBefore(codeBefore.toString());
        invoke.insertAfter(codeAfter.toString());
        classInfo.flag = true;
        classInfo.setModified();
    }

}
