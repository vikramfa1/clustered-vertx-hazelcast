package com.example.demo.configuration;

import com.example.demo.model.UserPolicies;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Data
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "user-policies")
public class UserPolicyConfiguartion {

    List<UserPolicies> userPoliciesList;

    public Optional<UserPolicies> findUserPolicyByName(String name) {
        return userPoliciesList.stream().filter(a -> a.getName().equals(name)).findAny();
    }
}
