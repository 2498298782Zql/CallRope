package zql.CallRope.springBootDemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static zql.CallRope.springBootDemo.GlobalThreadPoolBuffer.GlabalThreadPoolUtils.shopPool;
import static zql.CallRope.springBootDemo.GlobalThreadPoolBuffer.GlabalThreadPoolUtils.threadPoolExecutor;
@RestController
@RequestMapping("/login")
public class loginController {

    @GetMapping("/test/user/zql/114514")
    public String login(){
        shopPool.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("我是业务线程池");
            }
        });
        System.out.println(Thread.currentThread().getContextClassLoader() + "   {context}");
        threadPoolExecutor.submit(() -> {
            System.out.println("我是lambda表达式");
        });
        return "hello zql";
    }

    @GetMapping("/unlogin")
    public String unlogin(){
        System.out.println("退出登录");
        threadPoolExecutor.submit(() -> {
            System.out.println("hello world");
            System.out.println("退出登录");
        });
        return "success";
    }
}
