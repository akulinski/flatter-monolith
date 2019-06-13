package com.flatter.server.config;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import domain.Questionnaireable;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean
    @Qualifier("stringQuestionnaireableCache")
    public Cache<String, Questionnaireable> createCache() {
        return CacheBuilder.newBuilder()
                .maximumSize(1000)
                .build();
    }

    @Bean
    public Cache createGenericCache() {
        return CacheBuilder.newBuilder()
            .maximumSize(1000)
            .build();
    }


/*
    @Bean
    public Cache<String, CentroidCluster<Questionnaireable>> createClusterCache() {
        return CacheBuilder.newBuilder()
                .maximumSize(1000)
                .build();
    }
*/

    @Bean
    @Qualifier("questionnaireableCentroidClusterCache")
    public Cache<Questionnaireable, CentroidCluster<Questionnaireable>> createQuestionableToClusterCache() {
        return CacheBuilder.newBuilder()
                .maximumSize(1000)
                .build();
    }
}
