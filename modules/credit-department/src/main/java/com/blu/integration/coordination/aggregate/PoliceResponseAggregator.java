package com.blu.integration.coordination.aggregate;

import java.util.stream.Collectors;

import org.springframework.integration.aggregator.MessageGroupProcessor;
import org.springframework.integration.store.MessageGroup;
import org.springframework.messaging.Message;

import com.blu.integration.model.PoliceResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PoliceResponseAggregator implements MessageGroupProcessor {

    private final String discriminatorHeaderName;

    @Override
    public Object processMessageGroup(final MessageGroup group) {
        return group.getMessages().stream()
            .filter(message -> PoliceResponse.class.isInstance(message.getPayload()))
            .collect(Collectors.toMap(m -> m.getHeaders().get(discriminatorHeaderName), Message::getPayload));
    }
}
