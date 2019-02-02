package com.xcoder.dueros.service;

import com.baidu.dueros.certificate.Certificate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xcoder.dueros.baidu.MyTaxBot;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class TestBotService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestBotService.class);

    public String selectObject(RoutingContext context) throws Exception {


        Map req = new HashMap(8);
        req.put("type", "LaunchRequest");
        req.put("requestId", "12312312");
        req.put("timestamp", "1231312311231");

        Map map = new HashMap<>(16);
        map.put("version", "1.0");
        map.put("session", null);
        map.put("context", null);
        map.put("request", req);


        String params = new ObjectMapper().writeValueAsString(map);

        Certificate certificate = new Certificate("", "", params);

        MyTaxBot bot = new MyTaxBot(certificate);
        bot.disableVerify();
        String responseJson = bot.run();
        return responseJson;
    }

}
