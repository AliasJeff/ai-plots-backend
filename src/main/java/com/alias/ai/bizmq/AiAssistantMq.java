package com.alias.ai.bizmq;

import com.alias.ai.manager.AiManager;
import com.alias.ai.model.entity.AiAssistant;
import com.alias.ai.model.enums.ChartStatusEnum;
import com.alias.ai.service.AiAssistantService;
import com.google.gson.Gson;
import com.alias.ai.constant.BiMqConstant;
import com.alias.ai.constant.CommonConstant;
import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * AI 问答 MQ 队列
 */
@Slf4j
@Component
@AllArgsConstructor
public class AiAssistantMq {

    private final static Gson GSON = new Gson();

    @Resource
    private AiManager aiManager;
    @Resource
    private AiAssistantService aiAssistantService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = BiMqConstant.AI_QUESTION_QUEUE),
                    exchange = @Exchange(value = BiMqConstant.AI_QUESTION_EXCHANGE_NAME),
                    key = BiMqConstant.AI_QUESTION_ROUTING_KEY,
                    arguments = {
                            @Argument(name = "x-dead-letter-exchange", value = BiMqConstant.AI_DLX_EXCHANGE_NAME)
                    }
            )
    )
    public void handle(Message message, Channel channel) throws IOException {
        AiAssistant aiAssistant = null;
        try {
            String data = new String(message.getBody());
            aiAssistant = GSON.fromJson(data, AiAssistant.class);
            String questionGoal = aiAssistant.getQuestionGoal();
            // 调用 AI
            String result = aiManager.doAiChat(CommonConstant.AI_MODEL_ID, questionGoal);
            aiAssistant.setQuestionResult(result);
            aiAssistant.setQuestionStatus(ChartStatusEnum.SUCCEED.getValue());
            aiAssistantService.updateById(aiAssistant);
            // 交付标签，消息id
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            // 拒绝后丢弃
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            if (aiAssistant != null) {
                aiAssistant.setQuestionStatus(ChartStatusEnum.FAILED.getValue());
                aiAssistantService.updateById(aiAssistant);
            }
        }
    }
}
