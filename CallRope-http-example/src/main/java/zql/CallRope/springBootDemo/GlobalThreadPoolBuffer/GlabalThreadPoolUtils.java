package zql.CallRope.springBootDemo.GlobalThreadPoolBuffer;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.Executors;

public class GlabalThreadPoolUtils {
    public TransmittableThreadLocal<String> local = new TransmittableThreadLocal<>();
    public InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal();
    public void test(){
        Object o = inheritableThreadLocal.get();

    }

}
