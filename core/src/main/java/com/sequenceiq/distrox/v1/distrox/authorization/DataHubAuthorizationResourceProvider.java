package com.sequenceiq.distrox.v1.distrox.authorization;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.sequenceiq.authorization.service.list.AbstractAuthorizationResourceProvider;
import com.sequenceiq.authorization.service.list.AuthorizationResource;
import com.sequenceiq.cloudbreak.api.endpoint.v4.common.StackType;
import com.sequenceiq.cloudbreak.service.stack.StackService;
import com.sequenceiq.cloudbreak.service.workspace.WorkspaceService;

@Component
public class DataHubAuthorizationResourceProvider extends AbstractAuthorizationResourceProvider {

    @Inject
    private StackService stackService;

    @Inject
    private WorkspaceService workspaceService;

    @Override
    protected List<AuthorizationResource> findByAccoundId(String accountId) {
        return stackService.getAsAuthorizationResources(workspaceService.getForCurrentUser().getId(), StackType.WORKLOAD);
    }

    @Override
    protected List<AuthorizationResource> findByAccoundIdAndCrns(String accountId, List<String> resourceCrns) {
        return stackService.getAsAuthorizationResourcesByCrns(workspaceService.getForCurrentUser().getId(), StackType.WORKLOAD, resourceCrns);
    }
}
