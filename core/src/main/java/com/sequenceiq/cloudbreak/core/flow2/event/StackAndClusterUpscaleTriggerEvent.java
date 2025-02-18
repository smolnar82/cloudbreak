package com.sequenceiq.cloudbreak.core.flow2.event;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.sequenceiq.cloudbreak.common.event.AcceptResult;
import com.sequenceiq.cloudbreak.common.type.ClusterManagerType;
import com.sequenceiq.cloudbreak.common.type.ScalingType;
import com.sequenceiq.cloudbreak.core.flow2.dto.NetworkScaleDetails;
import com.sequenceiq.common.api.adjustment.AdjustmentTypeWithThreshold;

import reactor.rx.Promise;

public class StackAndClusterUpscaleTriggerEvent extends StackScaleTriggerEvent {

    private final ScalingType scalingType;

    private final boolean singleMasterGateway;

    private final boolean kerberosSecured;

    private final boolean singleNodeCluster;

    private final boolean restartServices;

    private final ClusterManagerType clusterManagerType;

    public StackAndClusterUpscaleTriggerEvent(String selector, Long stackId, Map<String, Integer> hostGroupWithAdjustment, ScalingType scalingType,
            NetworkScaleDetails networkScaleDetails, AdjustmentTypeWithThreshold adjustmentTypeWithThreshold, String triggeredStackVariant) {
        super(selector, stackId, hostGroupWithAdjustment, Collections.emptyMap(), Collections.emptyMap(), networkScaleDetails, adjustmentTypeWithThreshold,
                triggeredStackVariant);
        this.scalingType = scalingType;
        singleMasterGateway = false;
        kerberosSecured = false;
        singleNodeCluster = false;
        restartServices = false;
        clusterManagerType = ClusterManagerType.CLOUDERA_MANAGER;
    }

    public StackAndClusterUpscaleTriggerEvent(String selector, Long stackId, Map<String, Integer> hostGroupWithAdjustment,
            Map<String, Set<Long>> hostGroupWithPrivateIds, Map<String, Set<String>> hostgroupWithHostnames, ScalingType scalingType,
            boolean singlePrimaryGateway,
            boolean kerberosSecured, Promise<AcceptResult> accepted, boolean singleNodeCluster,
            boolean restartServices, ClusterManagerType clusterManagerType, AdjustmentTypeWithThreshold adjustmentTypeWithThreshold,
            String triggeredStackVariant) {
        super(selector, stackId, hostGroupWithAdjustment, hostGroupWithPrivateIds, hostgroupWithHostnames, adjustmentTypeWithThreshold, triggeredStackVariant,
                accepted);
        this.scalingType = scalingType;
        singleMasterGateway = singlePrimaryGateway;
        this.kerberosSecured = kerberosSecured;
        this.singleNodeCluster = singleNodeCluster;
        this.restartServices = restartServices;
        this.clusterManagerType = clusterManagerType;
    }

    public ScalingType getScalingType() {
        return scalingType;
    }

    public boolean isSingleMasterGateway() {
        return singleMasterGateway;
    }

    public boolean isKerberosSecured() {
        return kerberosSecured;
    }

    public boolean isSingleNodeCluster() {
        return singleNodeCluster;
    }

    public boolean isRestartServices() {
        return restartServices;
    }

    public ClusterManagerType getClusterManagerType() {
        return clusterManagerType;
    }

    @Override
    public StackAndClusterUpscaleTriggerEvent setRepair() {
        super.setRepair();
        return this;
    }
}
