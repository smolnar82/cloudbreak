package com.sequenceiq.cloudbreak.structuredevent.service.telemetry.converter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.reflect.Whitebox;

import com.cloudera.thunderhead.service.common.usage.UsageProto;
import com.sequenceiq.cloudbreak.structuredevent.event.StructuredSyncEvent;
import com.sequenceiq.cloudbreak.structuredevent.service.telemetry.mapper.ClusterRequestProcessingStepMapper;

@ExtendWith(MockitoExtension.class)
public class StructuredSyncEventToCDPDatahubSyncConverterTest {

    private StructuredSyncEventToCDPDatahubSyncConverter underTest;

    @BeforeEach
    public void setUp() {
        underTest = new StructuredSyncEventToCDPDatahubSyncConverter();
        StructuredEventToCDPOperationDetailsConverter operationDetailsConverter = new StructuredEventToCDPOperationDetailsConverter();
        Whitebox.setInternalState(operationDetailsConverter, "appVersion", "version-1234");
        Whitebox.setInternalState(operationDetailsConverter, "clusterRequestProcessingStepMapper", new ClusterRequestProcessingStepMapper());
        Whitebox.setInternalState(underTest, "operationDetailsConverter", operationDetailsConverter);
        StructuredEventToCDPClusterDetailsConverter clusterDetailsConverter = new StructuredEventToCDPClusterDetailsConverter();
        Whitebox.setInternalState(clusterDetailsConverter, "clusterShapeConverter", new StructuredEventToCDPClusterShapeConverter());
        Whitebox.setInternalState(clusterDetailsConverter, "imageDetailsConverter", new StructuredEventToCDPImageDetailsConverter());
        Whitebox.setInternalState(clusterDetailsConverter, "versionDetailsConverter", new StructuredEventToCDPVersionDetailsConverter());
        Whitebox.setInternalState(underTest, "clusterDetailsConverter", clusterDetailsConverter);
        Whitebox.setInternalState(underTest, "syncDetailsConverter", new StructuredSyncEventToCDPSyncDetailsConverter());
        Whitebox.setInternalState(underTest, "statusDetailsConverter", new StructuredEventToCDPStatusDetailsConverter());
    }

    @Test
    public void testConvertWithNull() {
        UsageProto.CDPDatahubSync datahubSync = underTest.convert(null);

        Assertions.assertNotNull(datahubSync.getOperationDetails());
        Assertions.assertNotNull(datahubSync.getSyncDetails());
        Assertions.assertNotNull(datahubSync.getClusterDetails());
        Assertions.assertNotNull(datahubSync.getStatusDetails());
    }

    @Test
    public void testConvertWithEmptyStructuredSyncEvent() {
        StructuredSyncEvent structuredSyncEvent = new StructuredSyncEvent();
        UsageProto.CDPDatahubSync datahubSync = underTest.convert(structuredSyncEvent);

        Assertions.assertNotNull(datahubSync.getOperationDetails());
        Assertions.assertNotNull(datahubSync.getSyncDetails());
        Assertions.assertNotNull(datahubSync.getClusterDetails());
        Assertions.assertNotNull(datahubSync.getStatusDetails());
    }
}
