package com.alias.ai.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.alias.ai.model.dto.user.UserAddRequest;
import com.alias.ai.model.dto.user.UserQueryRequest;
import com.alias.ai.model.dto.user.UserRegisterRequest;
import com.alias.ai.model.dto.user.UserUpdateMyRequest;
import com.alias.ai.model.entity.User;
import com.alias.ai.model.vo.LoginUserVO;
import com.alias.ai.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务
 * 
 */
public interface UserService extends IService<User> {

    /**
     * 发送邮箱验证码
     * @param toEmail
     * @return
     */
    boolean sendEmail(String toEmail);

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 管理员添加用户
     *
     * @param userAddRequest
     * @return
     */
    long addUser(UserAddRequest userAddRequest);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    User getLoginUserPermitNull(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    boolean isAdmin(User user);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏的已登录用户信息
     *
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param userList
     * @return
     */
    List<UserVO> getUserVO(List<User> userList);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 用户修改自己的信息
     * @param userUpdateMyRequest
     * @param request
     * @return
     */
    boolean updateMyUser(UserUpdateMyRequest userUpdateMyRequest,
                                       HttpServletRequest request);

}
