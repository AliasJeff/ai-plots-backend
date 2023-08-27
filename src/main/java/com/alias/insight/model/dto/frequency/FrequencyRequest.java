package com.alias.insight.model.dto.frequency;

import lombok.Data;

import java.io.Serializable;

/**
 * 使用次数
 */
@Data
public class FrequencyRequest implements Serializable {
    private int frequency;
}
