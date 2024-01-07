package com.example.demo.handler;

import com.example.demo.configuration.BlockUrlListPolicyConfiguration;
import com.example.demo.router.ResponseService;
import io.vertx.rxjava.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BlockUrlListValidationHandler {

    @Autowired
    private BlockUrlListPolicyConfiguration configuration;

    @Autowired
    private ResponseService responseService;

    public void validateBlockUrlList(RoutingContext routingContext) {
        log.info("validating host {} in the blockedUrl List", (String)routingContext.get("ACTUAL_HOST"));
        if(configuration.findBlockDns(routingContext.get("ACTUAL_HOST"))) {
            log.info("actual host {} in the blockedUrl List", (String)routingContext.get("ACTUAL_HOST"));
            responseService.sendAccessDeniedResponse(routingContext);
            routingContext.end();
        }
        routingContext.next();
    }
}
