package com.xcoder.dueros.handler;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

/**
 * Error handler
 *
 * @author Chuck Lee
 */
public class ErrorHandler implements Handler<RoutingContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);

    @Override
    public void handle(RoutingContext routingContext) {
        int statusCode = routingContext.statusCode();

        LOGGER.debug("statusCode:" + statusCode);

        HttpServerResponse response = routingContext.response();
        response.setStatusCode(statusCode).end("~Oops服务器开小差了");
    }
}
