package com.example.demo.service;

import io.vertx.core.http.HttpServerOptions;

import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class Teststandalone {
    public static final String TLS_1_3 = "TLSv1.3";
    public static final String TLS_1_2 = "TLSv1.2";

    public static final String[] FIPS_COMPLIANT_CIPHERS_ORDERED = {
            "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384",
            "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256",
            "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384",
            "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256",
            "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA",
            "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA",
            "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384", // N-ssllabs
            "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256", // N-ssllabs
            // "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384", // N-bctls, ssllabs[3,4]
            "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256",
            "TLS_DHE_DSS_WITH_AES_128_GCM_SHA256", // N-ssllabs
            "TLS_DHE_DSS_WITH_AES_128_CBC_SHA256",
            "TLS_DHE_DSS_WITH_AES_256_CBC_SHA256",
            // "TLS_DHE_RSA_WITH_AES_128_GCM_SHA256", // N-ssllabs, N-bctls
            // "TLS_DHE_RSA_WITH_AES_128_CBC_SHA256", // N-bctls, ssllabs[3,4]
            // "TLS_DHE_RSA_WITH_AES_256_CBC_SHA256", // N-bctls, ssllabs[3,4]
            "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA",
            "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA",
            "TLS_RSA_WITH_AES_256_GCM_SHA384", // N-ssllabs
            "TLS_RSA_WITH_AES_128_GCM_SHA256",
            "TLS_RSA_WITH_AES_256_CBC_SHA256",
            "TLS_RSA_WITH_AES_128_CBC_SHA256",
            "TLS_RSA_WITH_AES_256_CBC_SHA",
            "TLS_RSA_WITH_AES_128_CBC_SHA"
    };
    public static final List<String> FIPS_COMPLIANT_CIPHERS_ORDERED_LIST = List.of(FIPS_COMPLIANT_CIPHERS_ORDERED);

    public static void main(String args[]) throws NoSuchAlgorithmException {

        HttpServerOptions options = new HttpServerOptions();
        options.getEnabledSecureTransportProtocols().forEach(a -> System.out.println("ce: "+a));
        Arrays.stream(SSLContext.getDefault().getSupportedSSLParameters().getProtocols())
                .map(a -> {System.out.println("ava: "+a); return a;})
                // Only TLSv1.2 is enabled for SEG FIPS mode.
                .filter(value -> !TLS_1_2.equalsIgnoreCase(value))
                .forEach(options::removeEnabledSecureTransportProtocol);
        options.addEnabledSecureTransportProtocol(TLS_1_3);
        options.getEnabledSecureTransportProtocols().forEach(a -> System.out.println("e: "+a));
        options.getEnabledCipherSuites().forEach(a -> System.out.println("suites: "+a));
        FIPS_COMPLIANT_CIPHERS_ORDERED_LIST.forEach(a -> options.addEnabledCipherSuite(a));
        options.getEnabledCipherSuites().forEach(a -> System.out.println("suites added: "+a));
        System.out.println(options.getEnabledCipherSuites() +","+options.getEnabledSecureTransportProtocols());
    }
}
