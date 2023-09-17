package com.alias.ai.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.alias.ai.common.ErrorCode;
import com.alias.ai.constant.CommonConstant;
import com.alias.ai.constant.UserConstant;
import com.alias.ai.exception.BusinessException;
import com.alias.ai.exception.ThrowUtils;
import com.alias.ai.mapper.UserMapper;
import com.alias.ai.model.dto.user.UserAddRequest;
import com.alias.ai.model.dto.user.UserQueryRequest;
import com.alias.ai.model.dto.user.UserRegisterRequest;
import com.alias.ai.model.dto.user.UserUpdateMyRequest;
import com.alias.ai.model.entity.AiFrequency;
import com.alias.ai.model.entity.User;
import com.alias.ai.model.entity.UserCode;
import com.alias.ai.model.enums.UserRoleEnum;
import com.alias.ai.model.vo.LoginUserVO;
import com.alias.ai.model.vo.UserVO;
import com.alias.ai.service.AiFrequencyService;
import com.alias.ai.service.UserCodeService;
import com.alias.ai.service.UserService;
import com.alias.ai.utils.SqlUtils;
import com.alias.ai.utils.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


import static com.alias.ai.constant.RedisConstant.LOGIN_USER_PREFIX;
import static com.alias.ai.constant.RedisConstant.REGISTER_EMAIL_PREFIX;
import static com.alias.ai.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务实现
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private AiFrequencyService aiFrequencyService;

    @Resource
    private UserCodeService userCodeService;

    @Resource
    private RedisTemplate redisTemplate;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public boolean sendEmail(String toEmail) {
        // 发件人电子邮箱
        String from = "zhexunchen@qq.com";

        // 指定发送邮件的主机为 smtp.qq.com
        String host = "smtp.qq.com";

        // 获取系统属性
        Properties properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);

        properties.put("mail.smtp.auth", "true");


        //阿里云服务器禁用25端口，所以服务器上改为465端口
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        properties.setProperty("mail.smtp.socketFactory.port", "465");

        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("zhexunchen@qq.com", "ualqybfjbbnhcaab"); //发件人邮件用户名、密码
            }
        });

        try {
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);

            // Set From: 头部头字段
            message.setFrom(new InternetAddress(from));

            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(toEmail));

            // Set Subject: 头部头字段
            message.setSubject("ALIAS-API开放平台验证码");

            // 设置消息体
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code: {}", code);
            redisTemplate.opsForValue().set(REGISTER_EMAIL_PREFIX + toEmail, code, 5, TimeUnit.MINUTES);
            message.setText("您的验证码是：" + code + "\n" + "五分钟内有效");

            // 发送消息
            Transport.send(message);
            return true;
        } catch (MessagingException mex) {
            mex.printStackTrace();
            return false;
        }
    }

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @Override
    public long userRegister(UserRegisterRequest userRegisterRequest) {
        String userAccount = userRegisterRequest.getUserAccount();
        String email = userRegisterRequest.getEmail();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String code = userRegisterRequest.getCode();
        // 校验
        if (StringUtils.isAnyBlank(userAccount, email, userPassword, checkPassword, code)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 6 || checkPassword.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        // 验证码校验
        if (!StringUtils.equals((String) redisTemplate.opsForValue().get(REGISTER_EMAIL_PREFIX + email), code)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误");
        }

        synchronized (userAccount.intern()) {
            // 账户不能重复
            isCodeAndAccountExist(userAccount);
            // 3. 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + userPassword).getBytes());

            // 4. 插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserName(userAccount);
            user.setEmail(email);
            user.setUserPassword(encryptPassword);
            user.setUserAvatar(UserConstant.DEFAULT_AVATAR);
            //user.setUserCode(userCode);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }

            // 用户注册，保存用户调用次数
            AiFrequency aiFrequency = new AiFrequency();
            aiFrequency.setUserId(user.getId());
            aiFrequencyService.save(aiFrequency);

            // 用户编号自增
            UserCode userCode = new UserCode();
            userCode.setUserId(user.getId());
            userCodeService.save(userCode);

            if (redisTemplate.opsForValue().get(REGISTER_EMAIL_PREFIX + email) != null) {
                // 删除验证码
                redisTemplate.delete(REGISTER_EMAIL_PREFIX + email);
            }

            return user.getId();
        }
    }

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return
     */
    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        if (user == null) {
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("email", userAccount);
            queryWrapper.eq("userPassword", encryptPassword);
            user = this.baseMapper.selectOne(queryWrapper);
        }
        // 用户不存在
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 3. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    /**
     * 管理员添加用户
     *
     * @param userAddRequest
     * @return
     */
    @Override
    public long addUser(UserAddRequest userAddRequest) {
        String userName = userAddRequest.getUserName();
        String userAccount = userAddRequest.getUserAccount();
        String userAvatar = userAddRequest.getUserAvatar();
        String userPassword = userAddRequest.getUserPassword();
        String userRole = userAddRequest.getUserRole();
        String email = userAddRequest.getEmail();
        String phone = userAddRequest.getPhone();
        String gender = userAddRequest.getGender();
        Integer userStatus = userAddRequest.getUserStatus();

        // 账户和编号不能重复
        isCodeAndAccountExist(userAccount);
        // 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + userPassword).getBytes());
        User user = new User();
        user.setUserName(userName);
        user.setUserAvatar(userAvatar);
        user.setUserRole(userRole);
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserStatus(userStatus);
        user.setEmail(email);
        user.setGender(gender);
        user.setPhone(phone);
        boolean result = this.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);

        // 用户编号自增
        UserCode code = new UserCode();
        code.setUserId(user.getId());
        userCodeService.save(code);

        return user.getId();
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUserPermitNull(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            return null;
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        return this.getById(userId);
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return isAdmin(user);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        if (user == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        redisTemplate.delete(LOGIN_USER_PREFIX + user.getId() + "_" + USER_LOGIN_STATE);
        return true;
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 用户修改自己的信息
     *
     * @param userUpdateMyRequest
     * @param request
     * @return
     */
    @Override
    public boolean updateMyUser(UserUpdateMyRequest userUpdateMyRequest, HttpServletRequest request) {

        User loginUser = this.getLoginUser(request);
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyRequest, user);
        user.setId(loginUser.getId());
        boolean result = this.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return true;
    }

    /**
     * 判断账号和编号是否重复
     *
     * @param userAccount
     */
    private void isCodeAndAccountExist(String userAccount) {
        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }
    }
}
