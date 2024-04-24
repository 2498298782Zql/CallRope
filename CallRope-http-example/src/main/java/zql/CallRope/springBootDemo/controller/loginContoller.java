package zql.CallRope.springBootDemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class loginContoller {

    @GetMapping("/test/user/zql/114514")
    public String login(){
        try {
            System.out.println("运行了login方法");
            Thread.sleep(3000l);
            System.out.println("睡了3秒");
            return "hello zql";
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello zql";
    }

}
