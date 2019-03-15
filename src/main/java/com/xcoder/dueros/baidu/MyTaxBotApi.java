package com.xcoder.dueros.baidu;

import com.baidu.dueros.certificate.Certificate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Tax bot api
 *
 * @author Chuck Lee
 */
public class MyTaxBotApi {

    /**
     * slf4j logback
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MyTaxBotApi.class);

    /**
     * 百度DuerOs 请求响应
     *
     * @param signature 签名
     * @param certURL   认证地址
     * @param requestId 百度api要求request id
     * @param timestamp 百度api要求时间戳
     * @param session   session
     * @param context   context
     * @return
     * @throws Exception
     */
    public static String run(String signature, String certURL, Object requestId, Object timestamp
            , Object session, Object context) throws Exception {
        final Map req = LaunchRequest(requestId, timestamp);
        final Map msg = MessageMap(session, context, req);
        final String message = writeValueAsString(msg);

        LOGGER.debug("Request message:" + message);
        final Certificate certificate = new Certificate(signature, certURL, message);
        final MyTaxBot bot = new MyTaxBot(certificate);

        // Debug 环境下关闭认证
        if (LOGGER.isDebugEnabled()) {
            bot.disableVerify();
        }

        String response = bot.run();

        LOGGER.debug("Bot response:" + response);
        return response;
    }

    /**
     * 请求参数封装（固定格式）
     *
     * @param session session
     * @param context context
     * @param request json格式对象
     * @return
     */
    public static Map MessageMap(Object session, Object context, Object request) {
        final Map msg = new HashMap<>(4);
        msg.put("version", "1.0");
        msg.put("session", session);
        msg.put("context", context);
        msg.put("request", request);
        return msg;
    }

    /**
     * 请求参数封装（固定格式）
     *
     * @param requestId requestId
     * @param timestamp timestamp
     * @return
     */
    public static Map LaunchRequest(Object requestId, Object timestamp) {
        final Map req = new HashMap(8);
        req.put("type", "LaunchRequest");
        req.put("requestId", requestId);
        req.put("timestamp", timestamp);
        return req;
    }

    /**
     * 对象序列化（Jackson）成字符串
     *
     * @param value value任意java对象
     * @return
     * @throws JsonProcessingException
     */
    public static String writeValueAsString(Object value) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(value);
    }

}
