package com;

import com.demo.UserInfoVO;

import java.util.ArrayList;
import java.util.List;

public class MockDataConstant {
    public static List<UserInfoVO> userMockList = new ArrayList<>();
    static {
        UserInfoVO user1 = new UserInfoVO(1, "张三", "111111");
        UserInfoVO user2 = new UserInfoVO(2, "李四", "222222");
        UserInfoVO user3 = new UserInfoVO(3, "王五", "333333");
        userMockList.add(user1);
        userMockList.add(user2);
        userMockList.add(user3);
    }
}
