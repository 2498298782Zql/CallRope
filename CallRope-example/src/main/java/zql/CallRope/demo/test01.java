package zql.CallRope.demo;

import zql.CallRope.point.TransmittableThreadLocal;
import zql.CallRope.point.TtlRunnable;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class test01 {

    public static ExecutorService executorService = Executors.newFixedThreadPool(1);
    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,5,5l,TimeUnit.SECONDS,new ArrayBlockingQueue<>(10));
    static TransmittableThreadLocal<String> transmittableThreadLocal = new TransmittableThreadLocal<>();
    static AtomicInteger integer = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {

        for(int i =0;i <10;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    testCode();
                }
            }).start();
        }
    }

    public static void testCode(){
        transmittableThreadLocal.set("父亲业务代码" + "tranceid: 10"+integer.addAndGet(1) + ",spanid : 1." + integer.get());
        System.out.println(transmittableThreadLocal.get());
        threadPoolExecutor.submit(TtlRunnable.get(new Runnable() {
            @Override
            public void run() {
                System.out.println("子业务代码" + transmittableThreadLocal.get());
            }
        },"123","1"));
    }
}
