package zql.CallRope.core.instrumentation.dubbo;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import zql.CallRope.core.instrumentation.ClassInfo;
import zql.CallRope.core.instrumentation.transformer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DubboProducerFilterTransformer implements transformer {
    private static Set<String> filterProviderInvoke = new HashSet<String>();

    static {
        filterProviderInvoke.add("com.filter.producerFilter");
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
        StringBuilder code = new StringBuilder();
        code.append("String traceId = com.alibaba.dubbo.rpc.RpcContext.getContext().getAttachment(\"rope-traceId\");\n");
        code.append("String pSpanId = com.alibaba.dubbo.rpc.RpcContext.getContext().getAttachment(\"rope-pSpanId\");");
        code.append("String spanId = com.alibaba.dubbo.rpc.RpcContext.getContext().getAttachment(\"rope-spanId\");");
        code.append("if(traceId == null || \"\".equals(traceId)){\n");
        code.append("   traceId = zql.CallRope.point.IDutils.TraceIdGenerator.generateTraceId();\n");
        code.append("}\n");
        code.append("String serviceInterfaceName = $1.getUrl().getServiceInterface();\n");
        code.append("String methodName = $2.getMethodName();\n");
        code.append("zql.CallRope.point.model.Span span = new zql.CallRope.point.model.SpanBuilder(traceId,spanId,pSpanId, serviceInterfaceName, methodName).build();\n");
        code.append("System.out.println(span + \"pppppppppppppp\");");
        invoke.insertBefore(code.toString());
        classInfo.flag = true;
        classInfo.setModified();
    }

}
