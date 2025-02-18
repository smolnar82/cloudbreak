package com.sequenceiq.cloudbreak.structuredevent.service.telemetry.converter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

import com.cloudera.thunderhead.service.common.usage.UsageProto;
import com.sequenceiq.cloudbreak.structuredevent.event.FlowDetails;
import com.sequenceiq.cloudbreak.structuredevent.event.cdp.CDPOperationDetails;
import com.sequenceiq.cloudbreak.structuredevent.event.cdp.environment.CDPEnvironmentStructuredFlowEvent;
import com.sequenceiq.cloudbreak.structuredevent.service.telemetry.mapper.CDPRequestProcessingStepMapper;

class CDPStructuredFlowEventToCDPOperationDetailsConverterTest {

    private CDPStructuredFlowEventToCDPOperationDetailsConverter underTest;

    @BeforeEach()
    public void setUp() {
        underTest = new CDPStructuredFlowEventToCDPOperationDetailsConverter();
        Whitebox.setInternalState(underTest, "appVersion", "version-1234");
        Whitebox.setInternalState(underTest, "cdpRequestProcessingStepMapper", new CDPRequestProcessingStepMapper());
    }

    @Test
    public void testConvertWithNull() {
        UsageProto.CDPOperationDetails details = underTest.convert(null, null);

        Assertions.assertEquals("", details.getAccountId());
        Assertions.assertEquals("", details.getResourceCrn());
        Assertions.assertEquals("", details.getResourceName());
        Assertions.assertEquals("", details.getInitiatorCrn());
        Assertions.assertEquals("", details.getCorrelationId());
        Assertions.assertEquals(UsageProto.CDPRequestProcessingStep.Value.UNSET, details.getCdpRequestProcessingStep());
        Assertions.assertEquals("", details.getFlowId());
        Assertions.assertEquals("", details.getFlowChainId());
        Assertions.assertEquals("", details.getFlowState());
        Assertions.assertEquals(UsageProto.CDPEnvironmentsEnvironmentType.Value.UNSET, details.getEnvironmentType());

        Assertions.assertEquals("version-1234", details.getApplicationVersion());
    }

    @Test
    public void testConversionWithNullOperation() {
        CDPEnvironmentStructuredFlowEvent cdpStructuredFlowEvent = new CDPEnvironmentStructuredFlowEvent();

        UsageProto.CDPOperationDetails details = underTest.convert(cdpStructuredFlowEvent, null);

        Assertions.assertEquals("", details.getAccountId());
        Assertions.assertEquals("", details.getResourceCrn());
        Assertions.assertEquals("", details.getResourceName());
        Assertions.assertEquals("", details.getInitiatorCrn());
        Assertions.assertEquals("", details.getCorrelationId());
        Assertions.assertEquals(UsageProto.CDPRequestProcessingStep.Value.UNSET, details.getCdpRequestProcessingStep());
        Assertions.assertEquals("", details.getFlowId());
        Assertions.assertEquals("", details.getFlowChainId());
        Assertions.assertEquals("", details.getFlowState());
        Assertions.assertEquals(UsageProto.CDPEnvironmentsEnvironmentType.Value.UNSET, details.getEnvironmentType());

        Assertions.assertEquals("version-1234", details.getApplicationVersion());
    }

    @Test
    public void testEnvironmentTypeConversion() {
        CDPEnvironmentStructuredFlowEvent cdpStructuredFlowEvent = new CDPEnvironmentStructuredFlowEvent();

        UsageProto.CDPOperationDetails details = underTest.convert(cdpStructuredFlowEvent, "AWS");
        Assertions.assertEquals(UsageProto.CDPEnvironmentsEnvironmentType.Value.AWS, details.getEnvironmentType());

        Assertions.assertThrows(IllegalArgumentException.class, () -> underTest.convert(cdpStructuredFlowEvent, "SOMETHING"));
    }

    @Test
    public void testInitProcessingType() {
        CDPEnvironmentStructuredFlowEvent cdpStructuredFlowEvent = new CDPEnvironmentStructuredFlowEvent();
        FlowDetails flowDetails = new FlowDetails();
        flowDetails.setFlowState("unknown");
        flowDetails.setNextFlowState("INIT_STATE");
        cdpStructuredFlowEvent.setFlow(flowDetails);

        UsageProto.CDPOperationDetails details = underTest.convert(cdpStructuredFlowEvent, null);

        Assertions.assertEquals(UsageProto.CDPRequestProcessingStep.Value.INIT, details.getCdpRequestProcessingStep());
        Assertions.assertEquals("", details.getFlowState());
    }

    @Test
    public void testFinalProcessingType() {
        CDPEnvironmentStructuredFlowEvent cdpStructuredFlowEvent = new CDPEnvironmentStructuredFlowEvent();
        FlowDetails flowDetails = new FlowDetails();
        flowDetails.setFlowState("unknown");
        flowDetails.setNextFlowState("ENV_CREATION_FAILED_STATE");
        cdpStructuredFlowEvent.setFlow(flowDetails);

        UsageProto.CDPOperationDetails details = underTest.convert(cdpStructuredFlowEvent, null);

        Assertions.assertEquals(UsageProto.CDPRequestProcessingStep.Value.FINAL, details.getCdpRequestProcessingStep());
        Assertions.assertEquals("", details.getFlowState());

        flowDetails.setNextFlowState("ENV_CREATION_FINISHED_STATE");

        details = underTest.convert(cdpStructuredFlowEvent, null);

        Assertions.assertEquals(UsageProto.CDPRequestProcessingStep.Value.FINAL, details.getCdpRequestProcessingStep());
        Assertions.assertEquals("", details.getFlowState());
    }

    @Test
    public void testSomethingElseProcessingType() {
        CDPEnvironmentStructuredFlowEvent cdpStructuredFlowEvent = new CDPEnvironmentStructuredFlowEvent();
        FlowDetails flowDetails = new FlowDetails();
        flowDetails.setNextFlowState("SOMETHING_ELSE");
        cdpStructuredFlowEvent.setFlow(flowDetails);

        UsageProto.CDPOperationDetails details = underTest.convert(cdpStructuredFlowEvent, null);

        Assertions.assertEquals(UsageProto.CDPRequestProcessingStep.Value.UNSET, details.getCdpRequestProcessingStep());
    }

    @Test
    public void testFlowAndFlowChainType() {
        CDPEnvironmentStructuredFlowEvent cdpStructuredFlowEvent = new CDPEnvironmentStructuredFlowEvent();
        FlowDetails flowDetails = new FlowDetails();
        flowDetails.setFlowId("flowId");
        flowDetails.setFlowChainId("flowChainId");
        flowDetails.setNextFlowState("ENV_CREATION_FINISHED_STATE");
        cdpStructuredFlowEvent.setFlow(flowDetails);

        CDPOperationDetails operationDetails = new CDPOperationDetails();
        operationDetails.setUuid("correlationId");
        cdpStructuredFlowEvent.setOperation(operationDetails);

        UsageProto.CDPOperationDetails details = underTest.convert(cdpStructuredFlowEvent, null);

        Assertions.assertEquals(UsageProto.CDPRequestProcessingStep.Value.FINAL, details.getCdpRequestProcessingStep());
        Assertions.assertEquals("flowId", details.getFlowId());
        Assertions.assertEquals("flowChainId", details.getFlowChainId());
        Assertions.assertEquals("correlationId", details.getCorrelationId());
    }

    @Test
    public void testNoFlowChain() {
        CDPEnvironmentStructuredFlowEvent cdpStructuredFlowEvent = new CDPEnvironmentStructuredFlowEvent();
        FlowDetails flowDetails = new FlowDetails();
        flowDetails.setFlowId("flowId");
        flowDetails.setFlowState("SOMETHING");
        flowDetails.setNextFlowState("ENV_CREATION_FINISHED_STATE");
        cdpStructuredFlowEvent.setFlow(flowDetails);

        UsageProto.CDPOperationDetails details = underTest.convert(cdpStructuredFlowEvent, null);

        Assertions.assertEquals(UsageProto.CDPRequestProcessingStep.Value.FINAL, details.getCdpRequestProcessingStep());
        Assertions.assertEquals("flowId", details.getFlowId());
        Assertions.assertEquals("flowId", details.getFlowChainId());
        Assertions.assertEquals("", details.getFlowState());
    }

    @Test
    public void testFlowStateOnlyFilledOutInCaseOfFailure() {
        CDPEnvironmentStructuredFlowEvent cdpStructuredFlowEvent = new CDPEnvironmentStructuredFlowEvent();
        FlowDetails flowDetails = new FlowDetails();
        cdpStructuredFlowEvent.setFlow(flowDetails);

        flowDetails.setFlowState("FLOW_STATE");
        flowDetails.setNextFlowState("ENV_CREATION_FAILED_STATE");
        UsageProto.CDPOperationDetails details = underTest.convert(cdpStructuredFlowEvent, null);
        Assertions.assertEquals("FLOW_STATE", details.getFlowState());

        flowDetails.setFlowState("FLOW_STATE");
        flowDetails.setNextFlowState("DOWNSCALE_FAIL_STATE");
        details = underTest.convert(cdpStructuredFlowEvent, null);
        Assertions.assertEquals("FLOW_STATE", details.getFlowState());

        flowDetails.setFlowState("FLOW_STATE");
        flowDetails.setNextFlowState("ENV_CREATION_FINISHED_STATE");
        details = underTest.convert(cdpStructuredFlowEvent, null);
        Assertions.assertEquals("", details.getFlowState());

        flowDetails.setFlowState("FLOW_STATE");
        flowDetails.setNextFlowState("INIT_STATE");
        details = underTest.convert(cdpStructuredFlowEvent, null);
        Assertions.assertEquals("", details.getFlowState());

        flowDetails.setFlowState(null);
        flowDetails.setNextFlowState("ENV_CREATION_FAILED_STATE");
        details = underTest.convert(cdpStructuredFlowEvent, null);
        Assertions.assertEquals("", details.getFlowState());

        flowDetails.setFlowState(null);
        flowDetails.setNextFlowState("DOWNSCALE_FAIL_STATE");
        details = underTest.convert(cdpStructuredFlowEvent, null);
        Assertions.assertEquals("", details.getFlowState());

        flowDetails.setFlowState("unknown");
        flowDetails.setNextFlowState("ENV_CREATION_FAILED_STATE");
        details = underTest.convert(cdpStructuredFlowEvent, null);
        Assertions.assertEquals("", details.getFlowState());

        flowDetails.setFlowState("unknown");
        flowDetails.setNextFlowState("DOWNSCALE_FAIL_STATE");
        details = underTest.convert(cdpStructuredFlowEvent, null);
        Assertions.assertEquals("", details.getFlowState());

        flowDetails.setFlowState("FLOW_STATE");
        flowDetails.setNextFlowState(null);
        details = underTest.convert(cdpStructuredFlowEvent, null);
        Assertions.assertEquals("", details.getFlowState());
    }
}