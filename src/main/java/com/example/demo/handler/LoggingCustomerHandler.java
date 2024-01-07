package com.example.demo.handler;

import com.example.demo.model.Customer;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.rxjava.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoggingCustomerHandler implements Handler<RoutingContext> {
    @Override
    public void handle(RoutingContext routingContext) {
        Customer customer = routingContext.get("custData");
        log.info("data: "+customer.id+","+customer.name);
            routingContext.response().putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .end(Json.encodePrettily(customer));
        }
}
