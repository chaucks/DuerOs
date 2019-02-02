package com.xcoder.dueros.handler;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

/**
 * Common response handler
 *
 * @author Chuck Lee
 */
public class ResponseHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "text/plain;charset=utf-8");
        routingContext.next();
    }

}
