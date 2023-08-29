package com.alias.ai.client;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.alias.ai.common.BaseResponse;
import com.alias.ai.model.entity.DevChatRequest;
import com.alias.ai.model.entity.DevChatResponse;
import com.alias.ai.utils.SignUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class GPTClient {
    private static final String HOST = "https://www.yucongming.com/api/dev";
    private final String accessKey;
    private final String secretKey;

    public GPTClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public BaseResponse<DevChatResponse> doChat(DevChatRequest devChatRequest) {
        String url = HOST + "/chat";
        String json = JSONUtil.toJsonStr(devChatRequest);
        String result = ((HttpRequest)HttpRequest.post(url).addHeaders(this.getHeaderMap(json))).body(json).execute().body();
        TypeReference<BaseResponse<DevChatResponse>> typeRef = new TypeReference<BaseResponse<DevChatResponse>>() {
        };
        return (BaseResponse)JSONUtil.toBean(result, typeRef, false);
    }

    private Map<String, String> getHeaderMap(String body) {
        Map<String, String> hashMap = new HashMap();
        hashMap.put("accessKey", this.accessKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        String encodedBody = URLEncodeUtil.encode(body, StandardCharsets.UTF_8);
        hashMap.put("body", encodedBody);
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000L));
        hashMap.put("sign", SignUtils.genSign(encodedBody, this.secretKey));
        return hashMap;
    }

    // 测试
    public static void main(String[] args) {
        String accessKey = "ggi3ek77xpobxuj5trqqsbtxf90desqw";
        String secretKey = "1f9mbspntrxclnndviyf63h3rd0x32ey";
        com.alias.ai.client.GPTClient yuCongMingClient = new com.alias.ai.client.GPTClient(accessKey, secretKey);
        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(1651468516836098050L);
        devChatRequest.setMessage("鄧紫棋的歌");
        BaseResponse<DevChatResponse> devChatResponseBaseResponse = yuCongMingClient.doChat(devChatRequest);
        System.out.println(devChatResponseBaseResponse);
        DevChatResponse data = (DevChatResponse)devChatResponseBaseResponse.getData();
        if (data != null) {
            String content = data.getContent();
            System.out.println(content);
        }

    }
}
