package com.flatter.server.domain;

import org.springframework.context.ApplicationEvent;

public class ClusterEvent extends ApplicationEvent {

    public ClusterEvent(Object source) {
        super(source);
    }
}
