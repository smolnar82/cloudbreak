package com.sequenceiq.authorization.service.list;

import java.util.Optional;

public class ResourceWithParentResource {

    private final String resourceCrn;

    private final Optional<String> parentResourceCrn;

    public ResourceWithParentResource(String resourceCrn, Optional<String> parentResourceCrn) {
        this.resourceCrn = resourceCrn;
        this.parentResourceCrn = parentResourceCrn;
    }

    public String getResourceCrn() {
        return resourceCrn;
    }

    public Optional<String> getParentResourceCrn() {
        return parentResourceCrn;
    }
}
