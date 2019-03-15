package com.xcoder.dueros.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * Vertx http服务的handler
 * 本handler主要用于设置response相关的参数
 * 所有对应路由的http请求其返回的response
 * 都会采用这里设定的配置返回响应
 *
 * @author Chuck Lee
 */
public class ResponseHandler implements Handler<RoutingContext> {

    /**
     * Http response content type
     */
    public static final String RESPONSE_CONTENT_TYPE = "text/plain;charset=utf-8";

    @Override
    public void handle(RoutingContext ctx) {
        ctx.response().putHeader("content-type", RESPONSE_CONTENT_TYPE);
        // 进入下一个handler处理http请求
        ctx.next();
    }

}
