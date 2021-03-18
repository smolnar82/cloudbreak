package com.sequenceiq.cloudbreak.cloud.gcp.client;

import org.apache.http.impl.client.AIMDBackoffManager;
import org.apache.http.impl.client.DefaultBackoffStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.api.client.http.apache.ApacheHttpTransport;
import com.sequenceiq.cloudbreak.cloud.gcp.tracing.GcpTracingInterceptor;

@Configuration
public class GcpHttpClientConfig {

    private static final long TIMEOUT = 5L;

    @Bean
    public ApacheHttpTransport gcpApacheHttpTransport(GcpTracingInterceptor gcpTracingInterceptor) {
        DefaultHttpClient defaultHttpClient = ApacheHttpTransport.newDefaultHttpClient();
        defaultHttpClient.addRequestInterceptor(gcpTracingInterceptor);
        defaultHttpClient.addResponseInterceptor(gcpTracingInterceptor);
        AIMDBackoffManager backoffManager = new AIMDBackoffManager(new PoolingHttpClientConnectionManager());
        defaultHttpClient.setBackoffManager(backoffManager);
        defaultHttpClient.setConnectionBackoffStrategy(new DefaultBackoffStrategy());
        defaultHttpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandlerWithLog());
        HttpParams params = defaultHttpClient.getParams();
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT * 1000);
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, TIMEOUT * 1000);
        return new ApacheHttpTransport(defaultHttpClient);
    }
}
