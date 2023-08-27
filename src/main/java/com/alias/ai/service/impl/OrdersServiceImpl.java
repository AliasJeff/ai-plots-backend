package com.alias.ai.service.impl;

import com.alias.ai.mapper.OrdersMapper;
import com.alias.ai.model.entity.Orders;
import com.alias.ai.service.OrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author MA_dou
* @description 针对表【orders(充值订单表)】的数据库操作Service实现
* @createDate 2023-07-06 20:36:41
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService {

}




