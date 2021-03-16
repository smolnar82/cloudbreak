package com.sequenceiq.authorization.service.list;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

public abstract class AbstractAuthorizationResourceProvider implements AuthorizationResourceProvider {

    private static final int SELECT_IN_TRESHOLD = 10;

    protected abstract List<AuthorizationResource> findByAccoundId(String accountId);

    protected abstract List<AuthorizationResource> findByAccoundIdAndCrns(String accountId, List<String> resourceCrns);

    @Override
    public List<ResourceWithParentResource> findResources(String accountId, List<String> resourceCrns) {
        if (CollectionUtils.isEmpty(resourceCrns)) {
            return List.of();
        }
        List<AuthorizationResource> authorizationResources;
        if (resourceCrns.size() <= SELECT_IN_TRESHOLD) {
            authorizationResources = findByAccoundIdAndCrns(accountId, resourceCrns);
        } else {
            Set<String> crnsSet = new HashSet<>(resourceCrns);
            authorizationResources = findByAccoundId(accountId)
                    .stream()
                    .filter(a -> crnsSet.contains(a.getResourceCrn()))
                    .collect(Collectors.toList());
        }
        return authorizationResources.stream()
                .map(a -> new ResourceWithParentResource(a.getResourceCrn(), a.getParentResourceCrn()))
                .collect(Collectors.toList());
    }
}
