package zql.CallRope.springBootDemo.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;


import static com.alibaba.dubbo.common.Constants.CONSUMER;

@Activate(group = CONSUMER)
public class dubboFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        com.alibaba.dubbo.rpc.RpcContext.getContext().setAttachment("key-test", "value");
        com.alibaba.dubbo.rpc.RpcContext.getContext().setAttachment("zql", "yes");
        System.out.println("invoke++++++++++++:  consumer" );
        zql.CallRope.point.model.Span spanTrace = (zql.CallRope.point.model.Span)zql.CallRope.point.Trace.spanTtl.get();
        System.out.println("------------------------------------------------------------------------------------------------------");
        System.out.println("invoke++++++++++++:  consumer" );
        System.out.println("invoke++++++++++++:  consumer" );
        return invoker.invoke(invocation);
    }
}
