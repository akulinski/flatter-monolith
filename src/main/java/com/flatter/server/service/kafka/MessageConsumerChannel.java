package com.flatter.server.service.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface MessageConsumerChannel {

    String CHANNEL = "input";

    @Input
    SubscribableChannel subscribableChannel();

    @Output("outputChannel")
    MessageChannel anOutput();
}
