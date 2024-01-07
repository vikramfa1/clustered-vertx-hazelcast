package com.example.demo.configuration;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ClientAuth;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.PfxOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

import static com.example.demo.service.Teststandalone.FIPS_COMPLIANT_CIPHERS_ORDERED;
import static java.util.Base64.getDecoder;

@Component
@Slf4j
public class SegVertxServerOptions {

    @Bean
    public HttpServerOptions vertxServerOptions1() {
        HttpServerOptions serverOptions = new HttpServerOptions();
        PfxOptions pfxOptions = new PfxOptions();
        pfxOptions.setPassword("awvmware331!");
        pfxOptions.setPath("/Users/svigneshwar/Documents/Applications/VertxDemoApp/src/main/resources/rootCA.pfx");
        serverOptions.setSsl(true).setPfxKeyCertOptions(pfxOptions);
        Stream.of(FIPS_COMPLIANT_CIPHERS_ORDERED).forEach(serverOptions::addEnabledCipherSuite);
        serverOptions.addEnabledSecureTransportProtocol("TLSv1.1");
        serverOptions.addEnabledSecureTransportProtocol("TLSv1.2");
        serverOptions.addEnabledSecureTransportProtocol("TLSv1.3");
        log.info("configured https server options with keystore");
        setPfxTrustStoreOptions(serverOptions);
        serverOptions.setClientAuth(ClientAuth.REQUIRED);
        return serverOptions;
    }

    private void setPfxTrustStoreOptions(HttpServerOptions httpServerOptions) {
        final PfxOptions pfxOptions = new PfxOptions();
        final String keyStorePath = "/Users/svigneshwar/Documents/Applications/VertxDemoApp/src/main/resources/client-keystore.pfx";
        log.info("Trust-store certificates absolute path = {}", keyStorePath);
        pfxOptions.setPath(keyStorePath);
        pfxOptions.setPassword("password");
        httpServerOptions.setPfxTrustOptions(pfxOptions);
        log.info("configured https server options with keystore");
    }

    //@Bean
    public HttpServerOptions vertxServerOptions() {
        HttpServerOptions serverOptions = new HttpServerOptions();
        log.info("reached here options");
        return serverOptions;
    }

}
