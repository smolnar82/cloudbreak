package com.sequenceiq.cloudbreak.telemetry.monitoring;

public class MonitoringAgentConfiguration {

    private String user;

    private Integer port;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}