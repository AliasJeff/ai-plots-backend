package com.alias.ai.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.alias.ai.service.AiAssistantService;
import com.alias.ai.mapper.AiAssistantMapper;
import com.alias.ai.model.entity.AiAssistant;
import org.springframework.stereotype.Service;

/**
* 
* @description 针对表【ai_assistant(AI 问答助手信息表)】的数据库操作Service实现
* @createDate 2023-06-25 18:54:46
*/
@Service
public class AiAssistantServiceImpl extends ServiceImpl<AiAssistantMapper, AiAssistant> implements AiAssistantService {

}
