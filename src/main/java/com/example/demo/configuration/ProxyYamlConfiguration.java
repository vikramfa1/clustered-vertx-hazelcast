package com.example.demo.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Data
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "proxy")
public class ProxyYamlConfiguration {

    private HashMap<String, String> urlMapping;

    public String getActualDNSFromProxy(String proxyDNS) {
        return urlMapping.get(proxyDNS);
    }
}
