package com.example.demo.handler;

import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.ext.web.RoutingContext;
import io.vertx.rxjava.ext.web.client.HttpRequest;
import io.vertx.rxjava.ext.web.client.WebClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.net.*;
import java.time.Duration;


@Component
@Slf4j
public class URLRewriteHandler {

    @Autowired
    Vertx vertx;

    public void setRequestBodyContext(RoutingContext routingContext) {
        String customerId = routingContext.pathParam("id");
        log.info("uri: "+routingContext.request().absoluteURI());
        //routingContext.redirect("http://localhost:8080/redirect/sampleAPI" ).end();
        createBuffer(routingContext);
    }

    public void getURLDataResponse(RoutingContext routingContext) {
        WebClient webClient = createWebclientWithOptions(vertx);
        HttpRequest<Buffer> reqBuffer = webClient.requestAbs(routingContext.request().method(), getActualURL(routingContext.request().absoluteURI(), routingContext.get("ACTUAL_HOST")).toString());
        Buffer buffer = routingContext.get("REQUEST_BUFFER");
        log.info("buffer: "+buffer);
        reqBuffer
                .putHeaders(routingContext.request().headers())
                .rxSendBuffer(buffer)
                .doOnSuccess(response -> System.out.println("[" + Thread.currentThread().getName() + "] service 1: response received"))
                .subscribe((a) -> routingContext.response()
                        .setStatusCode(201)
                        .end(a.body()), err -> {
                    log.error("err: "+err.getMessage(), err);
                    routingContext.response()
                            .setStatusCode(500)
                            .end("failure");
                });

        /*Single<HttpResponse<Buffer>> respBuffer = reqBuffer.rxSendBuffer(buffer);
        respBuffer.subscribe((a) -> routingContext.response().end(a.body()), ex ->
        {
            log.error("error occured: "+ex.getMessage(), ex);
            routingContext.response().close();
        });*/
        //routingContext.reroute("http://localhost:8080/redirect/sampleAPI");
    }
    private URI getActualURL(String proxyURI, String actualHost) {
        URL url;
        try {
            url = new URL(proxyURI);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        URI actualUrl;
        try {
            URIBuilder uriBuilder = new URIBuilder(url.toURI());
            uriBuilder.setHost(actualHost);
            uriBuilder.setScheme("http");
            uriBuilder.setPort(8080);
            actualUrl = uriBuilder.build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        log.info("actual url: "+actualUrl);
        return actualUrl;
    }
    private void createBuffer(RoutingContext routingContext) {
        final Buffer buffer = Buffer.buffer();

        routingContext.request().bodyHandler(totalBuffer -> {
            System.out.println("Full body received, length = " + totalBuffer.length());
            if(totalBuffer.length()==0) {
                log.info("empty buffer: " + totalBuffer);
            } else {
                buffer.appendBuffer(totalBuffer);
                log.info("buffer: " + buffer);
                log.info("a: " + totalBuffer.toJsonObject());
            }
            routingContext.put("REQUEST_BUFFER", buffer);
            routingContext.next();

        });

        /*routingContext.request().toObservable().subscribe(v -> {
            log.info("buffer1: " + buffer);
            buffer.appendBuffer(v);
            log.info("buffer: " + buffer);
            log.info("a: " + v.toJsonObject());
            routingContext.put("REQUEST_BUFFER", buffer);
            routingContext.next();
        }, ex -> {
            log.error("error", ex);
        }, () -> {
            routingContext.put("REQUEST_DATA", buffer);
        });*/

    }

    private WebClient createWebclientWithOptions(Vertx vertx) {
        final WebClientOptions options = new WebClientOptions();
        options.setConnectTimeout((int)Duration.ofMinutes(1).toMillis());
        return WebClient.create(vertx, options);
    }

    public void serveAccessDeniedPage(RoutingContext routingContext) {
        routingContext.response()
                    .putHeader("content-type", MediaType.TEXT_HTML_VALUE)
                    .sendFile("/Users/svigneshwar/Documents/Applications/VertxDemoApp/src/main/resources/static/AccessDenied.html");
    }
}
