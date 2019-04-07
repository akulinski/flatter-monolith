package com.flatter.server.service.kafka;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;

@EnableBinding(value = {Source.class, MessageProducerChannel.class, MessageConsumerChannel.class})
public class MessagingConfiguration {

}
