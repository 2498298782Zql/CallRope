package zql.CallRope.core.instrumentation.dubbo;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import zql.CallRope.core.instrumentation.ClassInfo;
import zql.CallRope.core.instrumentation.transformer;
import zql.CallRope.point.model.Span;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DubboConsumerFilterTransformer implements transformer {
    private static final String TRACE_ID = "CallRopeTraceId";
    private static Set<String> filterConsumerInvoke = new HashSet<String>();

    static {
        filterConsumerInvoke.add("zql.CallRope.springBootDemo.filter.dubboFilter");
    }

    @Override
    public void doTransform(ClassInfo classInfo) throws IOException, NotFoundException, ClassNotFoundException, CannotCompileException {
        if(!filterConsumerInvoke.contains(classInfo.getClassName())){
            return;
        }
        CtClass ctClass = classInfo.getCtClass();
        if(ctClass == null){
            throw new ClassNotFoundException(classInfo.getClassName() + ":没有这个类");
        }
        CtMethod invoke = ctClass.getDeclaredMethod("invoke");
        if(invoke == null){
            throw new ClassNotFoundException(classInfo.getClassName() + ":缺少invoke方法");
        }
        StringBuilder code = new StringBuilder();
        code.append("zql.CallRope.point.model.Span spanTrace = (zql.CallRope.point.model.Span)zql.CallRope.point.Trace.spanTtl.get();\n");
        code.append("String traceId = spanTrace.traceId;\n");
        code.append("String pSpanId = spanTrace.spanId;");
        code.append("if(spanTrace == null){\n");
        code.append("   traceId = zql.CallRope.point.IDutils.TraceIdGenerator.generateTraceId();\n");
        code.append("}\n");
        code.append("com.alibaba.dubbo.rpc.RpcContext.getContext().setAttachment(\"rope-traceId\",traceId);\n");
        code.append("com.alibaba.dubbo.rpc.RpcContext.getContext().setAttachment(\"rope-pSpanId\",pSpanId);\n");
        code.append("com.alibaba.dubbo.rpc.RpcContext.getContext().setAttachment(\"rope-spanId\", String.valueOf(spanTrace.LevelSpanId()));\n");
        invoke.insertBefore(code.toString());
        classInfo.flag = true;
        classInfo.setModified();
    }
}
