package com.example.demo.configuration;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfigurationProvider {

    @Bean
    public HazelcastInstance createHazelcastInstance() {
        return Hazelcast.getAllHazelcastInstances().iterator().next();
    }
}
