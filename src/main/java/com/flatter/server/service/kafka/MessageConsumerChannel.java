package com.flatter.server.service.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface MessageConsumerChannel {

    String CHANNEL = "subscribableMessageChannel";

    @Input
    SubscribableChannel subscribableChannel();
}
