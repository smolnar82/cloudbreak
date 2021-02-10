package com.sequenceiq.freeipa.controller;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;

import com.sequenceiq.authorization.annotation.CheckPermissionByAccount;
import com.sequenceiq.authorization.resource.AuthorizationResourceAction;
import com.sequenceiq.freeipa.api.v1.util.UtilV1Endpoint;
import com.sequenceiq.freeipa.api.v1.util.model.UsedImagesListV1Response;
import com.sequenceiq.freeipa.service.UsedImagesProvider;

@Controller
public class UtilV1Controller implements UtilV1Endpoint {

    @Inject
    private UsedImagesProvider usedImagesProvider;

    @Override
    @CheckPermissionByAccount(action = AuthorizationResourceAction.COLLECT_USED_IMAGES)
    public UsedImagesListV1Response usedImages() {
        return usedImagesProvider.getUsedImages();
    }
}
