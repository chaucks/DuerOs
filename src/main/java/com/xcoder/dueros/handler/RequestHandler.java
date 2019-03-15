package com.xcoder.dueros.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Vertx http 通用request处理
 *
 * @author Chuck Lee
 */
public class RequestHandler implements Handler<RoutingContext> {

    /**
     * slf4j logback
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestHandler.class);

    @Override
    public void handle(RoutingContext ctx) {
        // 获取请求参数
        String showErrorPage = ctx.request().getParam("showErrorPage");
        if (StringUtils.isNotEmpty(showErrorPage)) {
            LOGGER.debug("Show error page:" + ctx.pathParam("page"));
            throw new RuntimeException("Show error page.");
        }
        // 进入下一个handler
        ctx.next();
    }
}
