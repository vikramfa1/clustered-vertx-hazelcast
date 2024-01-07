package com.example.demo.service;

import com.example.demo.router.SenderRouter;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.http.HttpServer;
import io.vertxbeans.rxjava.ContextRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import rx.Observable;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

//@Component
@Slf4j
public class VertxHttpServer {


    @Autowired
    SenderRouter senderRouter;

    @Autowired
    Vertx vertx;

    @Autowired
    ContextRunner contextRunner;

    @Autowired
    HttpServerOptions vertxServerOptions;

    @Value("${server.port}")
    private int port;

    @PostConstruct
    public void start() throws ExecutionException, InterruptedException, TimeoutException {
        contextRunner.executeBlocking(1,
                () -> startServer()
                        .doOnError(e -> log.error("Error while launching HTTP Server.", e))
                        .buffer(1), 5, TimeUnit.MINUTES);
    }

    public Observable<HttpServer> startServer() {
        return vertx.createHttpServer(vertxServerOptions)
                .requestHandler(senderRouter.apiRouter(vertx))
                .rxListen(port,"localhost").toObservable()
                .doOnCompleted(() -> log.info("Listening on {}", port))
                .doOnError(e -> log.error("Unable to listen on {}", port));
    }
}
