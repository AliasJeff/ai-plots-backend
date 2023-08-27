package com.alias.ai.service.impl;

import com.alias.ai.common.ErrorCode;
import com.alias.ai.exception.BusinessException;
import com.alias.ai.mapper.AlipayInfoMapper;
import com.alias.ai.model.entity.AiFrequencyOrder;
import com.alias.ai.model.entity.AlipayInfo;
import com.alias.ai.service.AiFrequencyOrderService;
import com.alias.ai.service.AlipayInfoService;
import com.alias.ai.utils.IdWorkerUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * @description 针对表【alipay_info(次数订单表)】的数据库操作Service实现
 * @createDate 2023-07-12 17:05:42
 */
@Service
public class AlipayInfoServiceImpl extends ServiceImpl<AlipayInfoMapper, AlipayInfo>
        implements AlipayInfoService {


    @Resource
    private AiFrequencyOrderService aiFrequencyOrderService;

    @Override
    public long getPayNo(long orderId, long userId) {

        AiFrequencyOrder getOrder = getOrder(orderId);
        Integer orderStatus = getOrder.getOrderStatus();
        if (orderStatus == 1) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "订单已支付");
        }
        if (orderStatus == 2) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "订单已过期，请重新生成订单");
        }
        Double totalAmount = getOrder.getTotalAmount();
        long payNo = IdWorkerUtils.getInstance().nextId();
        AlipayInfo alipayInfo = new AlipayInfo();
        alipayInfo.setAlipayAccountNo(payNo);
        alipayInfo.setUserId(userId);
        alipayInfo.setOrderId(orderId);
        alipayInfo.setTotalAmount(totalAmount);
        boolean save = this.save(alipayInfo);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return payNo;
    }

    public AiFrequencyOrder getOrder(long orderId) {
        if (orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        AiFrequencyOrder orderServiceById = aiFrequencyOrderService.getById(orderId);
        if (orderServiceById == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "没有这个订单");
        }
        return orderServiceById;
    }

}




