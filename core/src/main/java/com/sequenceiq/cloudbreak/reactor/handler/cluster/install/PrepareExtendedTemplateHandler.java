package com.sequenceiq.cloudbreak.reactor.handler.cluster.install;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.common.event.Selectable;
import com.sequenceiq.cloudbreak.core.cluster.ClusterBuilderService;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.install.PrepareExtendedTemplateFailed;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.install.PrepareExtendedTemplateRequest;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.install.PrepareExtendedTemplateSuccess;
import com.sequenceiq.flow.event.EventSelectorUtil;
import com.sequenceiq.flow.reactor.api.handler.EventHandler;

import reactor.bus.Event;
import reactor.bus.EventBus;

@Component
public class PrepareExtendedTemplateHandler implements EventHandler<PrepareExtendedTemplateRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrepareExtendedTemplateHandler.class);

    @Inject
    private EventBus eventBus;

    @Inject
    private ClusterBuilderService clusterBuilderService;

    @Override
    public String selector() {
        return EventSelectorUtil.selector(PrepareExtendedTemplateRequest.class);
    }

    @Override
    public void accept(Event<PrepareExtendedTemplateRequest> event) {
        Long stackId = event.getData().getResourceId();
        Selectable response;
        try {
            clusterBuilderService.prepareExtendedTemplate(stackId);
            response = new PrepareExtendedTemplateSuccess(stackId);
        } catch (RuntimeException e) {
            LOGGER.error("PrepareExtendedTemplateHandler step failed with the following message: {}", e.getMessage());
            response = new PrepareExtendedTemplateFailed(stackId, e);
        }
        eventBus.notify(response.selector(), new Event<>(event.getHeaders(), response));
    }
}
