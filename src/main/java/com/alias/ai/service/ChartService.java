package com.alias.ai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.alias.ai.model.dto.chart.GenChartByAiRequest;
import com.alias.ai.model.entity.Chart;
import com.alias.ai.model.vo.BiResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @description 针对表【chart(图表信息表)】的数据库操作Service
 * @createDate 2023-05-14 19:20:33
 */
public interface ChartService extends IService<Chart> {
    /**
     * AI生成图表 同步
     *
     * @param multipartFile       用户上传的文件信息
     * @param genChartByAiRequest 用户的需求
     * @param request             http request
     * @return BiResponse 处理后的ai生成内容
     */
    BiResponse genChartByAi(MultipartFile multipartFile, GenChartByAiRequest genChartByAiRequest, HttpServletRequest request);

    /**
     * AI生成图表 异步
     *
     * @param multipartFile
     * @param genChartByAiRequest
     * @param request
     * @return
     */
    BiResponse genChartByAiAsync(MultipartFile multipartFile, GenChartByAiRequest genChartByAiRequest, HttpServletRequest request);

    /**
     * AI生成图表 异步RabbitMQ消息队列
     *
     * @param multipartFile
     * @param genChartByAiRequest
     * @param request
     * @return
     */
    BiResponse genChartByAiAsyncMq(MultipartFile multipartFile, GenChartByAiRequest genChartByAiRequest, HttpServletRequest request);


    void handleChartUpdateError(long chartId, String execMessage);
}
