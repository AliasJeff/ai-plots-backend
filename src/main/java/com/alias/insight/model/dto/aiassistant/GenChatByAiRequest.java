package com.alias.insight.model.dto.aiassistant;

import lombok.Data;

import java.io.Serializable;

/**
 * 文件上传请求
 *
 * 
 */
@Data
public class GenChatByAiRequest implements Serializable {

    /**
     * 问题名称
     */
    private String questionName;

    /**
     * 问题概述
     */
    private String questionGoal;

    /**
     * 问题类型
     */
    private String questionType;

    private static final long serialVersionUID = 1L;
}