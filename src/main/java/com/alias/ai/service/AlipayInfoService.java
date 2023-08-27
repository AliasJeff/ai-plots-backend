package com.alias.ai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.alias.ai.model.entity.AlipayInfo;

/**
* 
* @description 针对表【alipay_info(次数订单表)】的数据库操作Service
* @createDate 2023-07-12 17:05:42
*/
public interface AlipayInfoService extends IService<AlipayInfo> {

    long getPayNo(long orderId, long userId);

}
