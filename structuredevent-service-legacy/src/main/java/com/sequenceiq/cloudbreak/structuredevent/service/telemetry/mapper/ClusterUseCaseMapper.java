package com.sequenceiq.cloudbreak.structuredevent.service.telemetry.mapper;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cloudera.thunderhead.service.common.usage.UsageProto;
import com.sequenceiq.cloudbreak.structuredevent.event.FlowDetails;

@Component
public class ClusterUseCaseMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterUseCaseMapper.class);

    @Inject
    private ClusterRequestProcessingStepMapper clusterRequestProcessingStepMapper;

    private Map<Pair, UsageProto.CDPClusterStatus.Value> firstStepUseCaseMap;

    @PostConstruct
    private void initUseCaseMaps() {
        firstStepUseCaseMap = new HashMap<>();
        firstStepUseCaseMap.put(Pair.of("ProvisionFlowEventChainFactory", "CloudConfigValidationFlowConfig"), UsageProto.CDPClusterStatus.Value.CREATE_STARTED);
        firstStepUseCaseMap.put(Pair.of("ProperTerminationFlowEventChainFactory", "ClusterTerminationFlowConfig"),
                UsageProto.CDPClusterStatus.Value.DELETE_STARTED);
        firstStepUseCaseMap.put(Pair.of("UpscaleFlowEventChainFactory", "StackUpscaleConfig"), UsageProto.CDPClusterStatus.Value.UPSCALE_STARTED);
        firstStepUseCaseMap.put(Pair.of("DownscaleFlowEventChainFactory", "ClusterDownscaleFlowConfig"), UsageProto.CDPClusterStatus.Value.DOWNSCALE_STARTED);
        firstStepUseCaseMap.put(Pair.of("StartFlowEventChainFactory", "StackStartFlowConfig"), UsageProto.CDPClusterStatus.Value.RESUME_STARTED);
        firstStepUseCaseMap.put(Pair.of("StopFlowEventChainFactory", "ClusterStopFlowConfig"), UsageProto.CDPClusterStatus.Value.SUSPEND_STARTED);
        firstStepUseCaseMap.put(Pair.of("ClusterRepairFlowEventChainFactory", "FlowChainInitFlowConfig"), UsageProto.CDPClusterStatus.Value.REPAIR_STARTED);
        firstStepUseCaseMap.put(Pair.of("UpgradeDatalakeFlowEventChainFactory", "SaltUpdateFlowConfig"), UsageProto.CDPClusterStatus.Value.UPGRADE_STARTED);
        firstStepUseCaseMap.put(Pair.of("UpgradeDistroxFlowEventChainFactory", "SaltUpdateFlowConfig"), UsageProto.CDPClusterStatus.Value.UPGRADE_STARTED);
        firstStepUseCaseMap.put(Pair.of("", "ClusterCertificateRenewFlowConfig"), UsageProto.CDPClusterStatus.Value.RENEW_PUBLIC_CERT_STARTED);
        firstStepUseCaseMap.put(Pair.of("", "CertRotationFlowConfig"), UsageProto.CDPClusterStatus.Value.RENEW_CLUSTER_INTERNAL_CERT_STARTED);
    }

    // At the moment we need to introduce a complex logic to figure out the use case
    public UsageProto.CDPClusterStatus.Value useCase(FlowDetails flow) {
        UsageProto.CDPClusterStatus.Value useCase = UsageProto.CDPClusterStatus.Value.UNSET;
        String rootFlowChainType = getRootFlowChainType(flow.getFlowChainType());
        if (clusterRequestProcessingStepMapper.isFirstStep(flow)) {
            useCase = firstStepToUseCaseMapping(rootFlowChainType, flow.getFlowType());
        } else if (clusterRequestProcessingStepMapper.isLastStep(flow)) {
            useCase = lastStepToUseCaseMapping(rootFlowChainType, flow.getFlowType(), flow.getFlowState());
        }
        LOGGER.debug("FlowDetails: {}, Usecase: {}", flow, useCase);
        return useCase;
    }

    //CHECKSTYLE:OFF: CyclomaticComplexity
    private UsageProto.CDPClusterStatus.Value firstStepToUseCaseMapping(String rootFlowChainType, String flowType) {
        UsageProto.CDPClusterStatus.Value useCase =
                firstStepUseCaseMap.getOrDefault(Pair.of(rootFlowChainType, flowType), UsageProto.CDPClusterStatus.Value.UNSET);
        LOGGER.debug("Mapping flow type to use-case: [flowchain: {}, flow: {}]: usecase: {}", rootFlowChainType, flowType, useCase);
        return useCase;
    }
    //CHECKSTYLE:ON

    //CHECKSTYLE:OFF: CyclomaticComplexity
    private UsageProto.CDPClusterStatus.Value lastStepToUseCaseMapping(String rootFlowChainType, String flowType, String flowState) {
        UsageProto.CDPClusterStatus.Value useCase = UsageProto.CDPClusterStatus.Value.UNSET;
        String rootFlowType = StringUtils.isNotEmpty(rootFlowChainType) ? rootFlowChainType : flowType;
        switch (rootFlowType) {
            case "ProvisionFlowEventChainFactory":
                useCase = getClusterStatus(flowState, "CLUSTER_CREATION_FINISHED_STATE",
                        UsageProto.CDPClusterStatus.Value.CREATE_FINISHED, UsageProto.CDPClusterStatus.Value.CREATE_FAILED);
                break;
            case "ProperTerminationFlowEventChainFactory":
                useCase = getClusterStatus(flowState, "TERMINATION_FINISHED_STATE",
                        UsageProto.CDPClusterStatus.Value.DELETE_FINISHED, UsageProto.CDPClusterStatus.Value.DELETE_FAILED);
                break;
            case "UpscaleFlowEventChainFactory":
                useCase = getClusterStatus(flowState, "FINALIZE_UPSCALE_STATE",
                        UsageProto.CDPClusterStatus.Value.UPSCALE_FINISHED, UsageProto.CDPClusterStatus.Value.UPSCALE_FAILED);
                break;
            case "DownscaleFlowEventChainFactory":
                useCase = getClusterStatus(flowState, "DOWNSCALE_FINISHED_STATE",
                        UsageProto.CDPClusterStatus.Value.DOWNSCALE_FINISHED, UsageProto.CDPClusterStatus.Value.DOWNSCALE_FAILED);
                break;
            case "StartFlowEventChainFactory":
                useCase = getClusterStatus(flowState, "CLUSTER_START_FINISHED_STATE",
                        UsageProto.CDPClusterStatus.Value.RESUME_FINISHED, UsageProto.CDPClusterStatus.Value.RESUME_FAILED);
                break;
            case "StopFlowEventChainFactory":
                useCase = getClusterStatus(flowState, "STOP_FINISHED_STATE",
                        UsageProto.CDPClusterStatus.Value.SUSPEND_FINISHED, UsageProto.CDPClusterStatus.Value.SUSPEND_FAILED);
                break;
            case "ClusterRepairFlowEventChainFactory":
                useCase = getClusterStatus(flowState, "FLOWCHAIN_FINALIZE_FINISHED_STATE",
                        UsageProto.CDPClusterStatus.Value.REPAIR_FINISHED, UsageProto.CDPClusterStatus.Value.REPAIR_FAILED);
                break;
            case "UpgradeDatalakeFlowEventChainFactory":
                useCase = getClusterStatus(flowState, "CLUSTER_UPGRADE_FINISHED_STATE",
                        UsageProto.CDPClusterStatus.Value.UPGRADE_FINISHED, UsageProto.CDPClusterStatus.Value.UPGRADE_FAILED);
                break;
            case "UpgradeDistroxFlowEventChainFactory":
                useCase = getClusterStatus(flowState, "FLOWCHAIN_FINALIZE_FINISHED_STATE",
                        UsageProto.CDPClusterStatus.Value.UPGRADE_FINISHED, UsageProto.CDPClusterStatus.Value.UPGRADE_FAILED);
                break;
            case "ClusterCertificateRenewFlowConfig":
                useCase = getClusterStatus(flowState, "CLUSTER_CERTIFICATE_RENEWAL_FINISHED_STATE",
                        UsageProto.CDPClusterStatus.Value.RENEW_PUBLIC_CERT_FINISHED, UsageProto.CDPClusterStatus.Value.RENEW_PUBLIC_CERT_FAILED);
                break;
            case "CertRotationFlowConfig":
                useCase = getClusterStatus(flowState, "CERT_ROTATION_FINISHED_STATE", UsageProto.CDPClusterStatus.Value.RENEW_CLUSTER_INTERNAL_CERT_FINISHED,
                        UsageProto.CDPClusterStatus.Value.RENEW_CLUSTER_INTERNAL_CERT_FAILED);
                break;
            default:
                LOGGER.debug("Flow state: {}", flowState);
        }
        LOGGER.debug("Mapping last flow state to use-case: [flowchain: {}, flow:{}, flowstate: {}]: {}", rootFlowChainType, flowType, flowState, useCase);
        return useCase;
    }
    //CHECKSTYLE:ON

    private String getRootFlowChainType(String flowChainTypes) {
        if (StringUtils.isNotEmpty(flowChainTypes)) {
            return flowChainTypes.split("/")[0];
        }
        return "";
    }

    private UsageProto.CDPClusterStatus.Value getClusterStatus(String flowState, String finishedFlowState, UsageProto.CDPClusterStatus.Value finishedStatus,
            UsageProto.CDPClusterStatus.Value failedStatus) {
        if (flowState.equals(finishedFlowState)) {
            return finishedStatus;
        } else if (flowState.contains("_FAIL")) {
            return failedStatus;
        }
        return UsageProto.CDPClusterStatus.Value.UNSET;
    }
}
