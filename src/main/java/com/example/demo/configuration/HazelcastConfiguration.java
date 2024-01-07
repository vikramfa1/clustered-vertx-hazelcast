package com.example.demo.configuration;

import com.example.demo.model.Customer;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class HazelcastConfiguration {

        public static final String CUSTOMER = "customer";

        @Autowired
        HazelcastInstance hazelcastInstance;



        public Customer put(int number, Customer car){
            IMap<Integer, Customer> map = hazelcastInstance.getMap(CUSTOMER);
            return map.putIfAbsent(number, car);
        }

        public Customer get(int key){
            IMap<Integer, Customer> map = hazelcastInstance.getMap(CUSTOMER);
            return map.get(key);
        }

    public Config createConfig() {
        Config config = new Config();
        config.addMapConfig(mapConfig());
        return config;
    }

    private MapConfig mapConfig() {
        MapConfig mapConfig = new MapConfig(CUSTOMER);
        mapConfig.setTimeToLiveSeconds(20);
        mapConfig.setMaxIdleSeconds(20);
        return mapConfig;
    }
}
