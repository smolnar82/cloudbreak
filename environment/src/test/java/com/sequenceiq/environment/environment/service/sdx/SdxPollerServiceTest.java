package com.sequenceiq.environment.environment.service.sdx;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sequenceiq.sdx.api.model.SdxClusterResponse;
import com.sequenceiq.sdx.api.model.SdxClusterStatusResponse;

@ExtendWith(MockitoExtension.class)
public class SdxPollerServiceTest {

    private static final String ENV_NAME = "envName";

    @InjectMocks
    private SdxPollerService underTest;

    @Mock
    private SdxService sdxService;

    @Mock
    private Consumer<String> consumer;

    @Test
    public void testGetExecuteSdxOperationsAndGetCrnsWhenShouldSkipTheConsumer() {

        SdxClusterResponse sdxClusterResponse = new SdxClusterResponse();
        sdxClusterResponse.setStatus(SdxClusterStatusResponse.STOPPED);
        when(sdxService.list(ENV_NAME)).thenReturn(List.of(sdxClusterResponse));

        List<String> actual = underTest.getExecuteSdxOperationsAndGetCrns(ENV_NAME, consumer, Set.of(SdxClusterStatusResponse.STOPPED));

        verify(consumer, never()).accept(any());
        Assertions.assertEquals(1, actual.size());
    }

    @Test
    public void testGetExecuteSdxOperationsAndGetCrnsWhenShouldNotSkipTheConsumer() {

        SdxClusterResponse sdxClusterResponse = new SdxClusterResponse();
        sdxClusterResponse.setStatus(SdxClusterStatusResponse.RUNNING);
        sdxClusterResponse.setCrn("crn");
        when(sdxService.list(ENV_NAME)).thenReturn(List.of(sdxClusterResponse));

        List<String> actual = underTest.getExecuteSdxOperationsAndGetCrns(ENV_NAME, consumer, Set.of(SdxClusterStatusResponse.STOPPED));

        verify(consumer).accept(any());
        Assertions.assertEquals(1, actual.size());
    }

    @Test
    public void testGetExecuteSdxOperationsAndGetCrnsWhenOneSkipedAndOneNot() {

        SdxClusterResponse sdxClusterResponse = new SdxClusterResponse();
        sdxClusterResponse.setStatus(SdxClusterStatusResponse.RUNNING);
        sdxClusterResponse.setCrn("crn");
        SdxClusterResponse sdxClusterResponse1 = new SdxClusterResponse();
        sdxClusterResponse1.setStatus(SdxClusterStatusResponse.STOPPED);
        sdxClusterResponse1.setCrn("crn1");
        when(sdxService.list(ENV_NAME)).thenReturn(List.of(sdxClusterResponse, sdxClusterResponse1));

        List<String> actual = underTest.getExecuteSdxOperationsAndGetCrns(ENV_NAME, consumer, Set.of(SdxClusterStatusResponse.STOPPED));

        verify(consumer).accept("crn");
        verify(consumer, never()).accept("crn1");
        Assertions.assertEquals(2, actual.size());
    }

}
