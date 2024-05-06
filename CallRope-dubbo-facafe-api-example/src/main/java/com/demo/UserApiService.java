package com.demo;

import java.util.List;

public interface UserApiService {
    /**
     * 添加人员
     */
    UserInfoVO saveUser(UserInfoVO user);
    /**
     * 获取人员列表
     */
    List<UserInfoVO> listUser();
}
