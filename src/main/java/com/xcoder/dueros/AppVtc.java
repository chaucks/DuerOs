package com.xcoder.dueros;

import com.xcoder.dueros.handler.ErrorHandler;
import com.xcoder.dueros.handler.RequestHandler;
import com.xcoder.dueros.handler.ResponseHandler;
import com.xcoder.dueros.handler.ServiceHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

/**
 * Vertx http server
 *
 * 浏览器打开
 * http://127.0.0.1:8080/login/my/tax/login.html
 * http://127.0.0.1:8080/login/my/tax/login.html?showErrorPage=1
 *
 * @author Chuck Lee
 */
public class AppVtc extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) {
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);

        router.route(HttpMethod.POST, "/login/my/tax/:page").method(HttpMethod.GET).handler(new RequestHandler())
                .handler(new ResponseHandler()).handler(new ServiceHandler())
                .failureHandler(new ErrorHandler());

        server.requestHandler(router).listen(8080, "127.0.0.1");
    }

    public static void main(String[] args) {
        // slf4j 设置
        System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.SLF4JLogDelegateFactory");
        // 部署verticle并启动http服务
        Vertx.vertx().deployVerticle(new AppVtc());
    }
}
