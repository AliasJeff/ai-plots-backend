package com.alias.ai.controller;

import com.alias.ai.common.BaseResponse;
import com.alias.ai.common.ErrorCode;
import com.alias.ai.common.ResultUtils;
import com.alias.ai.model.dto.frequency.FrequencyRequest;
import com.alias.ai.model.entity.AiFrequency;
import com.alias.ai.model.entity.User;
import com.alias.ai.model.vo.AiFrequencyVO;
import com.alias.ai.service.AiFrequencyService;
import com.alias.ai.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.alias.ai.exception.BusinessException;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/aiFrequency")
//@CrossOrigin(origins = "http://localhost:8000", allowCredentials = "true")
public class AiFrequencyController {

    @Resource
    private AiFrequencyService aiFrequencyService;

    @Resource
    private UserService userService;

    /**
     * 用户是否存在，若存在是否有调用次数
     *
     * @param request
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<AiFrequencyVO> getAiFrequency(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        QueryWrapper<AiFrequency> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", loginUser.getId());
        AiFrequency aiFrequency = aiFrequencyService.getOne(queryWrapper);
        if (aiFrequency == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "此id用户不存在");
        }
        Integer remainFrequency = aiFrequency.getRemainFrequency();

        if (remainFrequency < 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余次数不足1，请及时充值！");
        }
        AiFrequencyVO aiFrequencyVO = new AiFrequencyVO();
        BeanUtils.copyProperties(aiFrequency, aiFrequencyVO);
        return ResultUtils.success(aiFrequencyVO);
    }

    /**
     * 充值接口
     * @param frequency
     * @param request
     * @return
     */
    @PostMapping("/frequency")
    public BaseResponse<Long> AiFrequencyRecharge(FrequencyRequest frequency , HttpServletRequest request) {
        int frequency1 = frequency.getFrequency();
        if( frequency1 <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"您输入的充值次数错误");
        }
        User loginUser = userService.getLoginUser(request);
        QueryWrapper<AiFrequency> wrapper = new QueryWrapper<>();
        wrapper.eq("userId",loginUser.getId());
        AiFrequency aiFrequencyServiceOne = aiFrequencyService.getOne(wrapper);
        if(aiFrequencyServiceOne == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"还没有次数记录");
        }
        Integer leftNum = aiFrequencyServiceOne.getRemainFrequency();
        leftNum = leftNum + frequency1;
        aiFrequencyServiceOne.setRemainFrequency(leftNum);
        boolean b = aiFrequencyService.updateById(aiFrequencyServiceOne);
        if(!b){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"充值次数失败");
        }
        return ResultUtils.success(aiFrequencyServiceOne.getId());
    }

    /**
     * 每日签到领取调用次数
     * @param request
     * @return
     */
    @GetMapping("/sign")
    public BaseResponse<Boolean> signFrequency(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        Boolean result = aiFrequencyService.sign(userId);
        return ResultUtils.success(result);
    }
}
