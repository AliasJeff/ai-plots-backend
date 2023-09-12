package com.alias.ai.service;

import com.alias.ai.model.dto.text.GenTextTaskByAiRequest;

import com.alias.ai.model.entity.TextTask;
import com.baomidou.mybatisplus.extension.service.IService;
import com.alias.ai.model.entity.User;
import org.springframework.web.multipart.MultipartFile;

/**
* @description 针对表【text_task(文本任务表)】的数据库操作Service
* @createDate 2023-07-12 20:32:15
*/
public interface TextTaskService extends IService<TextTask> {

    /**
     * 获取准备分析的表数据(事务回滚)
     * @param multipartFile
     * @param genTextTaskByAiRequest
     * @param loginUser
     * @return
     */
    TextTask getTextTask(MultipartFile multipartFile, GenTextTaskByAiRequest genTextTaskByAiRequest, User loginUser);

    /**
     * 文本更新失败
     * @param textTaskId
     * @param execMessage
     */
    void handleTextTaskUpdateError(Long textTaskId, String execMessage);
}
