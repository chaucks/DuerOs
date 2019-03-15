package com.xcoder.dueros.handler;

import com.xcoder.dueros.baidu.MyTaxBotApi;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

/**
 * Service handler 业务handler
 *
 * @author Chuck Lee
 */
public class ServiceHandler implements Handler<RoutingContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceHandler.class);

    @Override
    public void handle(RoutingContext ctx) {
        HttpServerResponse res = ctx.response();
        ctx.vertx().executeBlocking(f -> {
            try {
                // 这里用简单的代码代替真实环境的service业务调用
                // XxxService.doSomething...;

                String requestId = "gyua2o123ksu7y5b41";
                String timestamp = System.currentTimeMillis() + "";
                String rst = MyTaxBotApi.run("", "", requestId, timestamp, null, null);

                f.complete(rst);
            } catch (Throwable t) {
                f.fail(t);
            }
        }, r -> {
            if (r.succeeded()) {
                res.end((String) r.result());
                return;
            }
            LOGGER.error(r.cause());
            res.end("服务器开小差了");
        });
    }
}
