package com.example.demo.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "global-policy")
public class BlockUrlListPolicyConfiguration {

    List<String> blockDnsList;

    public boolean findBlockDns(String host) {
        return blockDnsList.stream().anyMatch(actHost -> actHost.equals(host));
    }

}
