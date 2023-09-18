package com.alias.ai.service;

import com.alias.ai.model.entity.AiFrequency;
import com.baomidou.mybatisplus.extension.service.IService;

/**
*
* @description 针对表【ai_frequency(ai调用次数表)】的数据库操作Service
* @createDate 2023-07-11 20:47:00
*/
public interface AiFrequencyService extends IService<AiFrequency> {

    /**
     * 调用智能分析接口次数自动减一
     * @param userId
     * @return
     */
    boolean invokeAutoDecrease(long userId);

    /**
     * 查看用户有无调用次数
     * @param userId
     * @return
     */
    boolean hasFrequency(long userId);

    /**
     * 每日签到领取调用次数
     * @param userId
     * @return
     */
    boolean sign(Long userId);

}
