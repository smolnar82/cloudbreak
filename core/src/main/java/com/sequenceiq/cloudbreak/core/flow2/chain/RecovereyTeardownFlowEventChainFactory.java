package com.sequenceiq.cloudbreak.core.flow2.chain;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.common.event.Selectable;
import com.sequenceiq.cloudbreak.core.flow2.cluster.termination.ClusterTerminationEvent;
import com.sequenceiq.cloudbreak.core.flow2.stack.termination.StackTerminationEvent;
import com.sequenceiq.cloudbreak.reactor.api.event.recipe.CcmKeyDeregisterSuccess;
import com.sequenceiq.cloudbreak.reactor.api.event.stack.TerminationEvent;
import com.sequenceiq.flow.core.chain.FlowEventChainFactory;
import com.sequenceiq.flow.core.chain.config.FlowTriggerEventQueue;

@Component
public class RecovereyTeardownFlowEventChainFactory implements FlowEventChainFactory<TerminationEvent> {

    @Override
    public String initEvent() {
        return FlowChainTriggers.RECOVERY_TERMINATION_TRIGGER_EVENT;
    }

    @Override
    public FlowTriggerEventQueue createFlowTriggerEventQueue(TerminationEvent event) {
        Queue<Selectable> flowEventChain = new ConcurrentLinkedQueue<>();
        flowEventChain.add(new TerminationEvent(ClusterTerminationEvent.RECOVERY_TERMINATION_EVENT.event(), event.getResourceId(), event.getForced(),
                event.accepted()));

        flowEventChain.add(new CcmKeyDeregisterSuccess(StackTerminationEvent.RECOVERY_TERMINATION_EVENT.event(), event.getResourceId(),
                event.accepted()));
        return new FlowTriggerEventQueue(getName(), flowEventChain);
    }
}
