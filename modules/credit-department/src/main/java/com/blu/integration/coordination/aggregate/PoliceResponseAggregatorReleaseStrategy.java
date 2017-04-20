package com.blu.integration.coordination.aggregate;

import org.springframework.integration.aggregator.ReleaseStrategy;
import org.springframework.integration.store.MessageGroup;

public class PoliceResponseAggregatorReleaseStrategy implements ReleaseStrategy {

    @Override
    public boolean canRelease(final MessageGroup group) {
        return group.getMessages().size() == group.getSequenceSize();
    }
}
