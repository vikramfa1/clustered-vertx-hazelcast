package com.example.demo.router;

import com.example.demo.helper.AccessDeniedHelper;
import io.vertx.rxjava.ext.web.RoutingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ResponseService {
    public String[] browsersList = {"Chrome", "Mozilla"};

    @Autowired
    AccessDeniedHelper accessDeniedHelper;

    public void sendAccessDeniedResponse(RoutingContext routingContext) {
        // Get an UserAgentStringParser and analyze the requesting client
        String agent = routingContext.request().getHeader("User-Agent");
        if(Arrays.stream(browsersList).filter(a -> agent.contains(a)).count()>0) {
            accessDeniedHelper.serveAccessDeniedPage(routingContext);
        } else {
            accessDeniedHelper.serverEndpointUnauthorised(routingContext);
        }
        routingContext.end();
    }
}
