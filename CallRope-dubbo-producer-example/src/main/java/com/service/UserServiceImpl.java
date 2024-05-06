package com.service;

import com.MockDataConstant;
import com.alibaba.dubbo.config.annotation.Service;
import com.demo.UserApiService;
import com.demo.UserInfoVO;

import java.util.List;

@Service
public class UserServiceImpl implements UserApiService {

    /**
     * 添加人员
     */
    @Override
    public UserInfoVO saveUser(UserInfoVO user) {
        long timeMillis = System.currentTimeMillis();
        user.setId(MockDataConstant.userMockList.size() + 1);
        MockDataConstant.userMockList.add(user);
        System.out.println(user  +  ": " + timeMillis );
        return user;
    }

    /**
     * 获取人员列表
     */
    @Override
    public List<UserInfoVO> listUser() {
        System.out.println("listUser");
        return MockDataConstant.userMockList;
    }
}

