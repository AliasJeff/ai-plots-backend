package com.alias.insight.service;

import com.alias.insight.model.dto.user.UserRegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 用户服务测试
 *

 */
@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void userRegister() {

        UserRegisterRequest registerRequest = new UserRegisterRequest();
        registerRequest.setUserAccount("alias");
        registerRequest.setUserPassword("123456789");
        registerRequest.setCheckPassword("123456789");
        //registerRequest.setUserCode("001");
        //try {
        //    long result = userService.userRegister(registerRequest);
        //    Assertions.assertEquals(-1, result);
        //    registerRequest.setUserAccount("xiao");
        //    result = userService.userRegister(registerRequest);
        //    Assertions.assertEquals(-1, result);
        //} catch (Exception e) {

        //}
    }
}
