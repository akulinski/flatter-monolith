package com.flatter.server.service.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface MessageConsumerChannel {


    String CHANNEL_INPUT_CLUSTERING = "input_clustering";
    String CHANNEL_OUTPUT_CLUSTERING = "anoutput_clustering";


    String CHANNEL_INPUT = "input";
    String CHANNEL_OUTPUT = "anoutput";


    @Input(CHANNEL_INPUT)
    SubscribableChannel subscribableChannel();

    @Output(CHANNEL_OUTPUT)
    MessageChannel anOutput();

    @Input(CHANNEL_INPUT_CLUSTERING)
    SubscribableChannel clusteringChannel();

    @Output(CHANNEL_OUTPUT_CLUSTERING)
    MessageChannel outputClustering();
}
