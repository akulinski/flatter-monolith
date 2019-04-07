package com.flatter.server.service.kafka;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MessageProducerChannel {
    String CHANNEL = "messageChannel";

    @Output
    MessageChannel messageChannel();
}
