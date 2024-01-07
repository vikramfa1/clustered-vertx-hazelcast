package com.example.demo.handler;

import com.example.demo.model.Customer;
import com.example.demo.service.CustomerService;
import io.vertx.core.Promise;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.eventbus.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.example.demo.constants.ApplicationConstants.FETCH_CUSTOMER_REQUEST_VALUE;

@Component
@Slf4j
public class RequestResponseHandler  extends AbstractVerticle {

    @Autowired
    private CustomerService customerService;

    @Override
    public void start(Promise<Void> startFuture) {
        processEventConsumptions();
        startFuture.complete();
        //Deque<Integer> result = new ArrayDeque<>();
        //result.po
        double d = 2.0;
        //double a = d-3.0;
    }

    public void processEventConsumptions() {
        Queue<String> queue = new LinkedList<>();
        char[] s = {};
        int a = s.length;
        vertx.eventBus().consumer(FETCH_CUSTOMER_REQUEST_VALUE, this::onMessage);
    }

    private void onMessage(Message<Object> objectMessage) {
        final String customerId = objectMessage.body().toString();
        log.info("customer id received:{} ",customerId);
        Optional<Customer> customer = customerService.getCustomerId(Integer.parseInt(customerId));
        customer.ifPresentOrElse( customer1 -> {
            log.info("customer object retrieved: "+customer1);
            objectMessage.reply(customer1);
        }, ()-> {
            log.info("customer object failed:");
            objectMessage.fail(400, "retrieval failed");
        });
    }
}
