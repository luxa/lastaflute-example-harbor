/*
 * Copyright (C) au Commerce & Life, Inc. All Rights Reserved.
 */
package org.docksidestage.app.logic.aws.secretsmanager;

import javax.annotation.Resource;

import org.docksidestage.bizfw.aws.cache.AwsSecretsManagerCache;
import org.docksidestage.mylasta.direction.HarborConfig;

/**
 * Default Secrets Manager Client.
 * @author harada
 */
public class DefaultSecretsManagerClient implements AwsSecretsManagerClient {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private HarborConfig config;
    @Resource
    private AwsSecretsManagerClientBuildingLogic clientBuildingLogic;
    @Resource
    private AwsSecretsManagerCache secretManagerCache;

    // ===================================================================================
    //                                                                            Override
    //                                                                            ========
    @Override
    public AwsSecretsManagerClientBuildingLogic getSecretsManagerClientBuildingLogic() {
        return clientBuildingLogic;
    }

    @Override
    public AwsSecretsManagerCache getAwsSecretsManagerCache() {
        return secretManagerCache;
    }
}