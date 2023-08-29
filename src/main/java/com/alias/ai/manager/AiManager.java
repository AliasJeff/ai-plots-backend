package com.alias.ai.manager;

import com.alias.ai.client.GPTClient;
import com.alias.ai.common.BaseResponse;
import com.alias.ai.common.ErrorCode;
import com.alias.ai.config.AiModelConfig;
import com.alias.ai.exception.BusinessException;
import com.alias.ai.model.entity.DevChatRequest;
import com.alias.ai.model.entity.DevChatResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AiManager {

    @Resource
    private GPTClient gptClient;

    @Resource
    private AiModelConfig aiModelConfig;

    /**
     * AI 对话
     *
     * @param message 消息
     * @param modeId
     * @return
     */
    public String doChat(String message, Long modeId) {
        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(aiModelConfig.getModelId());
        devChatRequest.setMessage(message);
        BaseResponse<DevChatResponse> response = gptClient.doChat(devChatRequest);
        if (response == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI 响应错误");
        }
        return response.getData().getContent();
    }

    public String doAiChat(Long modelId, String message) {
        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(modelId);
        devChatRequest.setMessage(message);
        BaseResponse<DevChatResponse> response = gptClient.doChat(devChatRequest);
        if (response == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI 响应错误");
        }
        return response.getData().getContent();
    }
}
