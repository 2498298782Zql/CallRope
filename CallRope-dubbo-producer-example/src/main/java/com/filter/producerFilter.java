package com.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;

import static com.alibaba.dubbo.common.Constants.PROVIDER;


@Activate(group = PROVIDER)
public class producerFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
        String zql = invocation.getAttachment("zql");
        System.out.println(RpcContext.getContext().getAttachment("key-test1"));
        System.out.println(zql);
        return invoker.invoke(invocation);
    }
}
