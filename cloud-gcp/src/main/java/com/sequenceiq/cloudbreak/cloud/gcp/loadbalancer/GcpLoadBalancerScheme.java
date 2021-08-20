package com.sequenceiq.cloudbreak.cloud.gcp.loadbalancer;

import com.sequenceiq.cloudbreak.cloud.model.CloudLoadBalancer;
import com.sequenceiq.common.api.type.LoadBalancerType;

public enum GcpLoadBalancerScheme {
    INTERNAL("INTERNAL", LoadBalancerType.PRIVATE),
    EXTERNAL("EXTERNAL", LoadBalancerType.PUBLIC);

    private final String gcpType;
    private final LoadBalancerType cbType;

    GcpLoadBalancerScheme(String gcpType, LoadBalancerType cbType) {
        this.gcpType = gcpType;
        this.cbType = cbType;
    }

    public String getGcpType() {
        return gcpType;
    }

    public LoadBalancerType getCbType() {
        return cbType;
    }

    public static GcpLoadBalancerScheme getScheme(CloudLoadBalancer cloudLoadBalancer) {
        switch (cloudLoadBalancer.getType()) {
            case PUBLIC:
                return EXTERNAL;
            case PRIVATE:
            default:
                return INTERNAL;
        }
    }
}