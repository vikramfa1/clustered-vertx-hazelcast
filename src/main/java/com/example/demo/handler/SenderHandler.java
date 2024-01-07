package com.example.demo.handler;

import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.http.HttpHeaders;
import io.vertx.rxjava.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import static com.example.demo.constants.ApplicationConstants.FETCH_CUSTOMER_REQUEST_VALUE;
import static com.example.demo.constants.ApplicationConstants.FETCH_CUSTOMER_VALUE;

@Component
@Slf4j
public class SenderHandler {


    public void rxRequestToOtherEventsValue(RoutingContext routingContext) {
        Vertx vertx = Vertx.vertx();
        vertx.eventBus()
                .rxRequest(FETCH_CUSTOMER_REQUEST_VALUE, routingContext.pathParam("id")).subscribe(
                        message -> {
                            log.info("rxMessage processed and returned value by event bus consumer: "+message);
                            routingContext.response()
                                    .putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                    .setStatusCode(201)
                                    .end(Json.encodePrettily(message.body()));
                        });
    }

    public void requestToOtherEventsValue(RoutingContext routingContext) {
        Vertx vertx = Vertx.vertx();
        vertx.eventBus()
                .request(FETCH_CUSTOMER_REQUEST_VALUE, routingContext.pathParam("id"), (result) -> {
                    log.info("blocking request Message processed and returned value by event bus consumer: "+result);
                    if(result.succeeded()) {
                            routingContext.response()
                                    .putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                    .setStatusCode(201)
                                    .end(Json.encodePrettily(result.result().body()));
                        } else if(result.failed()) {
                        log.info("retrieval process failed for customer id: {}", routingContext.pathParam("id"));
                        ReplyException replyFailure = (ReplyException) result.cause();
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.put("error", replyFailure.getMessage());

                        routingContext.response()
                                .putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .setStatusCode(replyFailure.failureCode())
                                .end(Json.encodePrettily(jsonObject));
                    }
                });
    }

    public void getCustomerIdValuePublish(RoutingContext routingContext, Vertx vertx) {
        routingContext.response().end();
        vertx.eventBus()
                .publish(FETCH_CUSTOMER_REQUEST_VALUE, routingContext.pathParam("id"));
    }

    public void getCustomerIdValuesSend(RoutingContext routingContext) {
        Vertx vertx = Vertx.vertx();
        routingContext.response().end();
        vertx.eventBus()
                .send(FETCH_CUSTOMER_REQUEST_VALUE, routingContext.pathParam("id"));
    }
}
