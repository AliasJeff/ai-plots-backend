package com.alias.ai.constant;

/**
 * 用户常量
 * 
 */
public interface UserConstant {

    /**
     * 盐值，混淆密码
     */
    String SALT = "alias";

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "user_login";

    /**
     * 默认角色
     */
    String DEFAULT_ROLE = "user";

    /**
     * 管理员角色
     */
    String ADMIN_ROLE = "admin";

    /**
     * 被封号
     */
    String BAN_ROLE = "ban";

    /**
     * 默认头像
     */
    String DEFAULT_AVATAR = "https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png";
}
