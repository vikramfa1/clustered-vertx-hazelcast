package com.example.demo.router;

import com.example.demo.handler.*;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.web.Router;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SenderRouter {

    @Autowired
    ReceipientHandler receipientHandler;

    @Autowired
    SenderHandler senderHandler;

    @Autowired
    CustomerDataStoreHandler customerDataStoreHandler;

    @Autowired
    LoggingCustomerHandler loggingCustomerHandler;

    @Autowired
    URLRewriteHandler urlRewriteHandler;

    @Autowired
    UserPolicyValidationHandler userPolicyValidationHandler;

    @Autowired
    BlockUrlListValidationHandler blockUrlListValidationHandler;

    public Router apiRouter(Vertx vertx) {
        Router router = Router.router(vertx);

        router.route("/AccessDenied").handler(urlRewriteHandler::serveAccessDeniedPage);
        router.route().handler(userPolicyValidationHandler::validateUserPolicy);
        router.route().handler(blockUrlListValidationHandler::validateBlockUrlList);
        router.route().handler(urlRewriteHandler::setRequestBodyContext);
        router.route().handler(urlRewriteHandler::getURLDataResponse);
        router.get("/getCustomersHandlerRequest/:id").handler(customerDataStoreHandler::getData);
        router.get("/getCustomer").handler(receipientHandler::getCustomerValue);
        router.get("/getCustomerPublish/:id").handler(context -> senderHandler.getCustomerIdValuePublish(context, vertx));
        router.get("/getCustomerSend/:id").handler(senderHandler::getCustomerIdValuesSend);
        router.get("/getCustomersRxRequest/:id").handler(senderHandler::rxRequestToOtherEventsValue);
        router.get("/getCustomersRequest/:id").handler(senderHandler::requestToOtherEventsValue);
        router.post("/postCustomersRxRequest").handler(customerDataStoreHandler);
        router.post("/postCustomersRxRequest").handler(loggingCustomerHandler);
        router.get("/getCustomersHandlerRequest/:id").handler(customerDataStoreHandler::getData);
        return router;
    }
}
