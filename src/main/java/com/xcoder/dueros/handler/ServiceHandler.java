package com.xcoder.dueros.handler;

import com.xcoder.dueros.service.TestBotService;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

/**
 * Bot service
 *
 * @author Chuck Lee
 */
public class ServiceHandler implements Handler<RoutingContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceHandler.class);

    @Override
    public void handle(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        routingContext.vertx().executeBlocking(future -> {
            try {
                Object object = new TestBotService().selectObject(routingContext);
                byte[] bytes = ((String) object).getBytes();

                future.complete(bytes);
            } catch (Throwable t) {
                t.printStackTrace();
                future.fail(t);
            }
        }, res -> {
            if (res.succeeded()) {
                response.end(Buffer.buffer((byte[]) res.result()));
            } else {
                LOGGER.error(res.cause());
                response.end("服务器开小差了");
            }
        });
    }
}
