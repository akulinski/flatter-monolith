package com.flatter.server.service;

import com.flatter.server.config.STATUS;
import com.flatter.server.service.kafka.MessageConsumerChannel;
import com.google.common.cache.Cache;
import domain.events.ClusteringDataRequest;
import domain.events.LoadedClusteringDataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(MessageConsumerChannel.class)
public class JoiningService {

    private final org.springframework.messaging.MessageChannel messageChannel;

    private final Cache<ClusteringDataRequest, STATUS> statusCache;

    Logger logger = LoggerFactory.getLogger(JoiningService.class);

    public JoiningService(MessageConsumerChannel messageChannels, Cache<ClusteringDataRequest, STATUS> statusCache) {
        this.messageChannel = messageChannels.outputClustering();
        this.statusCache = statusCache;
    }

    public void postRequestForClusteringData(String requester) {

        logger.debug("Posting request for {}", requester);

        ClusteringDataRequest clusteringDataRequest = new ClusteringDataRequest();

        clusteringDataRequest.setRequester(requester);

        statusCache.put(clusteringDataRequest, STATUS.PENDING);

        messageChannel.send(MessageBuilder.withPayload(requester).build());
    }


    @StreamListener(MessageConsumerChannel.CHANNEL_INPUT)
    public void handleAggregation(LoadedClusteringDataResponse loadedClusteringDataResponse) {
        logger.debug("Got response for {}", loadedClusteringDataResponse.getRequester());
    }
}
