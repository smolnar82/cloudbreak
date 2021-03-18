package com.sequenceiq.cloudbreak.cloud.gcp.client;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DefaultHttpRequestRetryHandlerWithLog extends DefaultHttpRequestRetryHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultHttpRequestRetryHandlerWithLog.class);

    private static final int RETRY_COUNT = 5;

    public DefaultHttpRequestRetryHandlerWithLog() {
        super(RETRY_COUNT, false, List.of());
    }

    @Override
    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
        try {
            HttpUriRequest request = (HttpUriRequest) context.getAttribute("http.request");
            String route = context.getAttribute("http.route").toString();
            LOGGER.debug("GCP client request will be retried on {}{}, exception message: {}", route, request.getURI(), exception.getMessage());
        } catch (Exception e) {
            LOGGER.debug("Cannot parse the URI. GCP client request will be retried: {}", context, exception);
        }
        return super.retryRequest(exception, executionCount, context);
    }
}
