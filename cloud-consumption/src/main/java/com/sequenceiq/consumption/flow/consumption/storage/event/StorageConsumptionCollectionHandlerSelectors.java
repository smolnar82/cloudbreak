package com.sequenceiq.consumption.flow.consumption.storage.event;

import com.sequenceiq.flow.core.FlowEvent;

public enum StorageConsumptionCollectionHandlerSelectors implements FlowEvent {

    GET_CLOUD_CREDENTIAL_HANDLER,
    STORAGE_CONSUMPTION_COLLECTION_HANDLER,
    SEND_CONSUMPTION_EVENT_HANDLER;

    @Override
    public String event() {
        return name();
    }
}
