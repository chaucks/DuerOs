package com.xcoder.dueros.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * Error handler
 *
 * @author Chuck Lee
 */
public class ErrorHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext ctx) {

        ctx.response().setStatusCode(404).putHeader("content-type", "text/html;charset=utf-8").sendFile(ctx.pathParam("page"));

//        ctx.response().setStatusCode(404).end("~Oops服务器开小差了");
    }
}
