package com.example.demo.handler;

import com.example.demo.configuration.ProxyYamlConfiguration;
import com.example.demo.configuration.UserPolicyConfiguartion;
import com.example.demo.model.UserPolicies;
import com.example.demo.router.ResponseService;
import io.vertx.rxjava.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.security.cert.X509Certificate;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

@Component
@Slf4j
public class UserPolicyValidationHandler {

    @Autowired
    private UserPolicyConfiguartion userPolicyConfiguartion;

    @Autowired
    private ProxyYamlConfiguration proxyYamlConfiguration;

    @Autowired
    private ResponseService responseService;

    public void validateUserPolicy(RoutingContext routingContext) {
        X509Certificate x509Certificate = getCertificateDetails(routingContext);
        log.info("client certificate details: {}", x509Certificate);
        log.info("client certificate details getIssuerDN: {}", x509Certificate.getIssuerDN());
        log.info("client certificate details1 getIssuerDN.getName(): {}", x509Certificate.getIssuerDN().getName());
        String[] a = x509Certificate.getIssuerDN().getName().split(",");
        String cnName = a[0].split("=")[1];
        Optional<UserPolicies> userPoliciesOptional = userPolicyConfiguartion.findUserPolicyByName(cnName);
        if(userPoliciesOptional.isEmpty()) {
            responseService.sendAccessDeniedResponse(routingContext);
        } else {
            String uri = routingContext.request().absoluteURI();
            URL url;
            try {
                url = new URL(uri);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            String actualUrl = proxyYamlConfiguration.getActualDNSFromProxy(url.getHost());
            routingContext.put("ACTUAL_HOST", actualUrl);
            log.info("validating the host {} in the user specific list - {}", actualUrl, cnName);
            Optional<String> urlOptional = userPoliciesOptional.get().getBlockUrlList().stream().filter(v -> v.equals(actualUrl)).findAny();
            if(urlOptional.isPresent()) {
                log.info("host {} in the blockedList in the user specific list - {}", actualUrl, cnName);
                responseService.sendAccessDeniedResponse(routingContext);
            }

        }

        routingContext.next();
    }

    private X509Certificate getCertificateDetails(final RoutingContext context) {
        X509Certificate clientCert = null;
        try {
            final X509Certificate[] certs = context.request().getDelegate().peerCertificateChain();
            if (certs != null && certs.length > 0) {
                log.info("certificate chain of length '{}' presented by device.", certs.length);
                clientCert = certs[0];
            }
        } catch (final SSLPeerUnverifiedException ex) {
            log.warn("Unable to get Peer Certificate Chain, won't be able to determine certificate. Error: {}", ex.getMessage());
        }
        return clientCert;
    }
}
