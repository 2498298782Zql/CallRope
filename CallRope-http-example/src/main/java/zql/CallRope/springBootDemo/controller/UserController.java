package zql.CallRope.springBootDemo.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.demo.UserApiService;
import com.demo.UserInfoVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/user")
public class UserController {
    /**
     * 引用服务
     */
    @Reference
    private UserApiService userApiService;

    /**
     * 添加人员
     */
    @RequestMapping("/save")
    public Object saveUser(@RequestBody UserInfoVO user) {
        return userApiService.saveUser(user);
    }

    /**
     * 获取人员列表
     */
    @RequestMapping("/list")
    public Object listUser() {
        System.out.println(Thread.currentThread().getName() + "  业务");
        return userApiService.listUser();
    }
}
