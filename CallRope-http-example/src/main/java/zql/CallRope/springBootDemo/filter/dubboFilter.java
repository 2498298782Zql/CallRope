package zql.CallRope.springBootDemo.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;


import static com.alibaba.dubbo.common.Constants.CONSUMER;

@Activate(group = CONSUMER)
public class dubboFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        zql.CallRope.point.model.Span spanTrace = zql.CallRope.point.TraceInfos.spanTtl.get();
        System.out.println(spanTrace);
        return invoker.invoke(invocation);
    }
}
