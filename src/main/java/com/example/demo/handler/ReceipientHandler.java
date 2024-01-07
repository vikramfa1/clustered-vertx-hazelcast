package com.example.demo.handler;

import com.example.demo.service.CustomerService;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.eventbus.Message;
import io.vertx.rxjava.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class ReceipientHandler extends AbstractVerticle {

    @Autowired
    private CustomerService customerService;

    @Override
    public void start(Promise<Void> startFuture) {
        processEventConsumptions();
        startFuture.complete();
    }
    public void getCustomerValue(RoutingContext routingContext) {
        customerService.getCustomer().toObservable().subscribe(
                message -> {
                    routingContext.response()
                            .putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .setStatusCode(201)
                            .end(Json.encodePrettily(message));
                },
                throwable -> { routingContext.response().setStatusCode(500).end(throwable.toString());});
    }

    public void processEventConsumptions() {
        vertx.eventBus().consumer("dummy").toObservable().subscribe(this::onMessage);

    }

    private void onMessage(Message<Object> objectMessage) {
        final String messageBody = objectMessage.body().toString();
        log.info("system body: "+messageBody);
        objectMessage.reply("DONE");
    }


}
