/*
 * Copyright (C) au Commerce & Life, Inc. All Rights Reserved.
 */
package org.docksidestage.app.logic.aws.secretsmanager;

import org.docksidestage.app.logic.aws.AwsSdkExceptionHandler;
import org.docksidestage.bizfw.aws.cache.AwsSecretsManagerCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

/**
 * AWS Secrets Manager Access Logic.<br>
 * @author harada
 */
public interface AwsSecretsManagerClient extends AwsSdkExceptionHandler {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** ロガー */
    Logger logger = LoggerFactory.getLogger(AwsSecretsManagerClient.class);

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    /**
     * シークレット値取得.
     * @param secretId シークレットID (NotNull)
     * @return シークレット値 (NotNull)
     */
    default String getSecretValue(String secretId) {
        AwsSecretsManagerCache cache = getAwsSecretsManagerCache();
        String value = cache.get(secretId);
        if (value != null) {
            return value;
        }

        try (SecretsManagerClient client = buildSecretsManagerClient()) {
            final GetSecretValueResponse response = client.getSecretValue(builder -> {
                builder.secretId(secretId);
            });

            String secretValue = response.secretString();
            cache.set(secretId, value);
            return secretValue;
        } catch (final RuntimeException e) {
            final String message = exceptionHandler("failed to secret value, id=" + secretId, e);
            throw new RuntimeException(message, e); // TODO 後で変更
        }
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    // -----------------------------------------------------
    //                                       Build S3 client
    //                                       ---------------
    /**
     * Secrets Manager Client構築
     * @return AWS Secrets Manager client (NotNull)
     */
    default SecretsManagerClient buildSecretsManagerClient() {
        return getSecretsManagerClientBuildingLogic().build();
    }

    /**
     * Secrets Manager ClientBuildingLogic取得
     * @return AWS Secrets Manager client (NotNull)
     */
    AwsSecretsManagerClientBuildingLogic getSecretsManagerClientBuildingLogic();

    /**
     * Secrets Manager Cache取得
     * @return AWS Secrets Manager Cache (NotNull)
     */
    AwsSecretsManagerCache getAwsSecretsManagerCache();
}