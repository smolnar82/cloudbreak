package com.sequenceiq.cloudbreak.reactor.handler.cluster.upgrade.ccm;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.common.event.Selectable;
import com.sequenceiq.cloudbreak.core.flow2.cluster.ccm.upgrade.UpgradeCcmService;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.upgrade.ccm.UpgradeCcmRegisterClusterProxyRequest;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.upgrade.ccm.UpgradeCcmRegisterClusterProxyResult;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.upgrade.ccm.UpgradeCcmFailedEvent;
import com.sequenceiq.flow.event.EventSelectorUtil;
import com.sequenceiq.flow.reactor.api.handler.ExceptionCatcherEventHandler;
import com.sequenceiq.flow.reactor.api.handler.HandlerEvent;

import reactor.bus.Event;

@Component
public class RegisterClusterProxyHandler extends ExceptionCatcherEventHandler<UpgradeCcmRegisterClusterProxyRequest> {

    @Inject
    private UpgradeCcmService upgradeCcmService;

    @Override
    public String selector() {
        return EventSelectorUtil.selector(UpgradeCcmRegisterClusterProxyRequest.class);
    }

    @Override
    protected Selectable defaultFailureEvent(Long resourceId, Exception e, Event<UpgradeCcmRegisterClusterProxyRequest> event) {
        return new UpgradeCcmFailedEvent(resourceId, e);
    }

    @Override
    protected Selectable doAccept(HandlerEvent<UpgradeCcmRegisterClusterProxyRequest> event) {
        UpgradeCcmRegisterClusterProxyRequest request = event.getData();
        Long stackId = request.getResourceId();
        upgradeCcmService.registerClusterProxy(stackId);
        return new UpgradeCcmRegisterClusterProxyResult(stackId);
    }
}
