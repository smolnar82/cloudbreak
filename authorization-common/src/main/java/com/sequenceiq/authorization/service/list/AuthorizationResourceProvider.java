package com.sequenceiq.authorization.service.list;

import java.util.List;

public interface AuthorizationResourceProvider {
    List<ResourceWithParentResource> findResources(String accountId, List<String> resourceCrns);
}
