/*
 * Copyright (C) au Commerce & Life, Inc. All Rights Reserved.
 */
package org.docksidestage.app.logic.aws.secretsmanager;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;

import javax.annotation.Resource;

import org.docksidestage.mylasta.direction.HarborConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClientBuilder;

/**
 * AWS Secrets Manager Client Builder.<br>
 * <pre>
 * AWS Clientでいつも使用している@EnvDispatchは使用しない
 * Configの値をSecretsManagerで上書きするためにこのクラスは使われるので、
 * LastaFlute初期化前にDIコンテナからインスタンスを直接取得しているので
 * EnvDispatchは対応してない.
 * (試してみたら動かなかったので...)
 * </pre>
 * @author harada
 */
public class AwsSecretsManagerClientBuildingLogic {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    Logger logger = LoggerFactory.getLogger(AwsSecretsManagerClientBuildingLogic.class);

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private HarborConfig config;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    /**
     * AWS Secrets Manager クライアント生成.
     * @return Secure Manager クライアント (NotNull)
     */
    public SecretsManagerClient build() {
        logger.debug("Build SecretsManager Client. Region:{} Endpoint URL:{}", config.getAwsSecretsmanagerRegion(),
                config.getAwsSecretsmanagerEndpointUrl());
        return build(builder -> {});
    }

    /**
     * AWS Secrets Manager クライアント生成.
     * @param profile 接続プロファイル (NotNull)
     * @return Secure Manager クライアント (NotNull)
     */
    public SecretsManagerClient build(final String profile) {
        logger.debug("Build SecretsManager Client. Region:{} Endpoint URL:{} Profile:{}", config.getAwsSecretsmanagerRegion(),
                config.getAwsSecretsmanagerEndpointUrl(), profile);
        return build(builder -> {
            builder.credentialsProvider(ProfileCredentialsProvider.create(profile));
        });
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    /**
     * AWS Secrets Manager クライアント生成.
     * @param addtionalSetting 追加設定 (NotNull)
     * @return Secure Manager クライアント (NotNull)
     */
    protected SecretsManagerClient build(final Consumer<SecretsManagerClientBuilder> addtionalSetting) {
        final SecretsManagerClientBuilder builder = SecretsManagerClient.builder();
        builder.region(Region.of(config.getAwsSecretsmanagerRegion()));
        if (!config.getAwsSecretsmanagerEndpointUrl().isEmpty()) {
            try {
                builder.endpointOverride(new URI(config.getAwsSecretsmanagerEndpointUrl()));
            } catch (URISyntaxException e) {
                throw new RuntimeException("Invalid URI, " + config.getAwsSecretsmanagerEndpointUrl(), e);
            }
        }
        addtionalSetting.accept(builder);

        return builder.build();
    }
}
