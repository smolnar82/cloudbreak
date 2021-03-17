package com.sequenceiq.cloudbreak.cloud.azure;

import com.microsoft.azure.management.network.NetworkInterface;
import com.microsoft.azure.management.network.PublicIPAddress;
import com.sequenceiq.cloudbreak.cloud.azure.client.AzureClient;
import org.springframework.stereotype.Component;

@Component
class AzureVmPublicIpProvider {

    String getPublicIp(NetworkInterface networkInterface) {
        PublicIPAddress publicIpAddress = networkInterface.primaryIPConfiguration().getPublicIPAddress();

        if (publicIpAddress != null && publicIpAddress.ipAddress() != null) {
            return publicIpAddress.ipAddress();
        } else {
            return null;
        }
    }
}
