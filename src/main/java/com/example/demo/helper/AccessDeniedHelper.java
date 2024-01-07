package com.example.demo.helper;

import io.vertx.rxjava.ext.web.RoutingContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class AccessDeniedHelper {

    public void serveAccessDeniedPage(RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", MediaType.TEXT_HTML_VALUE)
                .sendFile("/Users/svigneshwar/Documents/Applications/VertxDemoApp/src/main/resources/static/AccessDenied.html");
    }

    public void serverEndpointUnauthorised(RoutingContext routingContext) {
        routingContext.response()
                .setStatusCode(403)
                .end("Access Denied");
    }


}
