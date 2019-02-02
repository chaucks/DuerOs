package com.xcoder.dueros;

import com.xcoder.dueros.handler.ErrorHandler;
import com.xcoder.dueros.handler.LoginHandler;
import com.xcoder.dueros.handler.ResponseHandler;
import com.xcoder.dueros.handler.ServiceHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

/**
 * Main handler
 *
 * @author Chuck Lee
 */
public class AppVtc extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) {
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);

        router.route(HttpMethod.POST, "/login").handler(new LoginHandler());

        router.route(HttpMethod.POST, "/login/ok/tax").method(HttpMethod.GET)
                .handler(new ServiceHandler())
                .handler(new ResponseHandler())
                .failureHandler(new ErrorHandler());

        server.requestHandler(router).listen(8080, "127.0.0.1");
    }

    public static void main(String[] args) {
        System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.SLF4JLogDelegateFactory");
        Vertx.vertx().deployVerticle(new AppVtc());
    }
}
