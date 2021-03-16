package com.sequenceiq.datalake.authorization;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.sequenceiq.authorization.service.list.AbstractAuthorizationResourceProvider;
import com.sequenceiq.authorization.service.list.AuthorizationResource;
import com.sequenceiq.datalake.repository.SdxClusterRepository;

@Component
public class DataLakeAuthorizationResourceProvider extends AbstractAuthorizationResourceProvider {

    @Inject
    private SdxClusterRepository sdxClusterRepository;

    @Override
    protected List<AuthorizationResource> findByAccoundId(String accountId) {
        return sdxClusterRepository.findAuthorizationResourcesByAccountId(accountId);
    }

    @Override
    protected List<AuthorizationResource> findByAccoundIdAndCrns(String accountId, List<String> resourceCrns) {
        return sdxClusterRepository.findAuthorizationResourcesByAccountIdAndCrns(accountId, resourceCrns);
    }
}
