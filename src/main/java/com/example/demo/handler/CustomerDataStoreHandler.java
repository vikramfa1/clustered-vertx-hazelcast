package com.example.demo.handler;

import com.example.demo.configuration.HazelcastConfiguration;
import com.example.demo.model.Customer;
import com.example.demo.service.CustomerService;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.rxjava.core.http.HttpServerRequest;
import io.vertx.rxjava.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomerDataStoreHandler implements Handler<RoutingContext> {


    @Autowired
    HazelcastConfiguration hazelcastConfiguration;

    @Autowired
    CustomerService customerService;

    @Override
    public void handle(RoutingContext routingContext) {
        HttpServerRequest httpRequest = routingContext.request();
        httpRequest.body(body -> {
            Customer cust = body.result().toJsonObject().mapTo(Customer.class);
            routingContext.put("custData", cust);
            log.info("obj: "+cust.name+","+cust.id);
                    hazelcastConfiguration.put(cust.id, cust);
                    customerService.addCustomer(cust);
            routingContext.next();
                });


    }

    public void getData(RoutingContext routingContext) {
        String customerId = routingContext.pathParam("id");
        Customer customer = hazelcastConfiguration.get(Integer.parseInt(customerId));

        if(customer == null) {
            System.out.println("data is null in cache");
            customer = customerService.getCustomerId(Integer.parseInt(customerId)).get();
        } else {
            log.info("data in cache: "+customer.name+","+customer.id);
        }
        routingContext.response().putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .end(Json.encodePrettily(customer));
    }



}
