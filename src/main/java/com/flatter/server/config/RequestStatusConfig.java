package com.flatter.server.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import domain.events.ClusteringDataRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class RequestStatusConfig {

    @Bean
    public Cache<ClusteringDataRequest, STATUS> createCache() {
        return CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();
    }
}
