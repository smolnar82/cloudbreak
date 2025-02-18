package com.sequenceiq.cloudbreak.core.flow2.cluster.stopstartus;

import com.sequenceiq.cloudbreak.cloud.event.instance.StopStartUpscaleStartInstancesResult;
import com.sequenceiq.cloudbreak.reactor.api.event.orchestration.StopStartUpscaleCommissionViaCMResult;
import com.sequenceiq.flow.core.FlowEvent;
import com.sequenceiq.flow.event.EventSelectorUtil;

public enum StopStartUpscaleEvent implements FlowEvent {

    STOPSTART_UPSCALE_TRIGGER_EVENT("STOPSTART_UPSCALE_TRIGGER_EVENT"),
    STOPSTART_UPSCALE_INSTANCES_STARTED_EVENT(EventSelectorUtil.selector(StopStartUpscaleStartInstancesResult.class)),
    STOPSTART_UPSCALE_INSTANCES_START_FAILED_EVENT(EventSelectorUtil.failureSelector(StopStartUpscaleStartInstancesResult.class)),
    STOPSTART_UPSCALE_CLUSTER_MANAGER_COMMISSIONED_EVENT(EventSelectorUtil.selector(StopStartUpscaleCommissionViaCMResult.class)),
    STOPSTART_UPSCALE_CLUSTER_MANAGER_COMMISSION_FAILED_EVENT(EventSelectorUtil.failureSelector(StopStartUpscaleCommissionViaCMResult.class)),
    STOPSTART_UPSCALE_FINALIZED_EVENT("STOPSTART_UPSCALE_FINALIZED_EVENT"),
    STOPSTART_UPSCALE_FAILURE_EVENT("STOPSTART_UPSCALE_FAILURE_EVENT"),
    STOPSTART_UPSCALE_FAIL_HANDLED_EVENT("STOPSTART_UPSCALE_FAIL_HANDLED_EVENT");

    private final String event;

    StopStartUpscaleEvent(String event) {
        this.event = event;
    }

    @Override
    public String event() {
        return event;
    }
}
