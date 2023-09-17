package com.alias.ai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 主类（项目启动入口）
 * 
 */
//@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@SpringBootApplication() // 开启redis
@MapperScan("com.alias.ai.mapper")
@EnableScheduling
@EnableTransactionManagement
@EnableCaching
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class AiPlotsApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiPlotsApplication.class, args);
    }
}
