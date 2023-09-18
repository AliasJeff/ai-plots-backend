package com.alias.ai.model.vo;

import lombok.Data;

@Data
public class AiFrequencyVO {

    /**
     * id
     */
    private Long id;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 总调用次数
     */
    private Integer totalFrequency;

    /**
     * 剩余调用次数
     */
    private Integer remainFrequency;
}
