/*
 * Copyright (C) au Commerce & Life, Inc. All Rights Reserved.
 */
package org.docksidestage.app.logic.aws;

import org.dbflute.helper.message.ExceptionMessageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.exception.SdkServiceException;

/**
  * AWS Client用例外メッセージビルダークラス.
  * @author harada
  */
public interface AwsSdkExceptionHandler {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** ロガー */
    Logger logger = LoggerFactory.getLogger(AwsSdkExceptionHandler.class);

    // ===================================================================================
    //                                                                   Exception Handler
    //                                                                   =================
    /**
     * AWS 例外ハンドラー.<br>
     * 例外情報から例外メッセージを作成しログに出力する
     * @param notice 通知情報 (NotNull)
     * @param e 例外情報 (NotNull)
     * @return ログメッセージ (NotNull)
     */
    default String exceptionHandler(final String notice, final Exception e) {
        // CHECKSTYLE:OFF @formatter:off
        final ExceptionMessageBuilder messageBuilder =
              e instanceof SdkServiceException ? buildSdkServiceExceptionMessage(notice, (SdkServiceException) e) :
              e instanceof SdkClientException  ? buildSdkClientExceptionMessage(notice,  (SdkClientException)  e) :
              e instanceof SdkException        ? buildSdkExceptionMessage(notice,  (SdkClientException)  e) :
                                                 buildExceptionMessage(notice, e);
        // CHECKSTYLE:ON @formatter:on

        final String message = messageBuilder.buildExceptionMessage();
        logger.warn(message);
        return message;
    }

    // -----------------------------------------------------
    //                               For SdkServiceException
    //                               -----------------------
    /**
     * AWS Serviceで例外が発生したときの処理
     * @param notice 通知情報 (NotNull)
     * @param e Amazon Service例外 (NotNull)
     * @return 例外メッセージビルダ　(NotNull)
     */
    default ExceptionMessageBuilder buildSdkServiceExceptionMessage(final String notice, final SdkServiceException e) {
        final ExceptionMessageBuilder messageBuilder = new ExceptionMessageBuilder();
        messageBuilder.addNotice(notice);
        messageBuilder.addNotice("Caught an SdkServiceException.");
        messageBuilder.addItem("Request ID");
        messageBuilder.addElement(e.requestId());
        messageBuilder.addItem("Extended Request ID");
        messageBuilder.addElement(e.extendedRequestId());
        messageBuilder.addItem("Statuc Code");
        messageBuilder.addElement(e.statusCode());
        messageBuilder.addItem("Error Message");
        messageBuilder.addElement(e.getMessage());

        return messageBuilder;
    }

    // -----------------------------------------------------
    //                                For SdkClientException
    //                                ----------------------
    /**
     * AWS SDK クライアントで例外が発生したときの処理
     * @param notice 通知情報 (NotNull)
     * @param e Amazon Client例外 (NotNull)
     * @return 例外メッセージビルダ　(NotNull)
     */
    default ExceptionMessageBuilder buildSdkClientExceptionMessage(final String notice, final SdkClientException e) {
        final ExceptionMessageBuilder messageBuilder = new ExceptionMessageBuilder();
        messageBuilder.addNotice(notice);
        messageBuilder.addNotice("Caught an SdkServiceException.");
        messageBuilder.addItem("Error Message");
        messageBuilder.addElement(e.getMessage());

        return messageBuilder;
    }

    // -----------------------------------------------------
    //                                      For SdkException
    //                                      -----------------
    /**
     * AWS SDKで例外が発生したときの処理
     * @param notice 通知情報 (NotNull)
     * @param e Amazon Client例外 (NotNull)
     * @return 例外メッセージビルダ　(NotNull)
     */
    default ExceptionMessageBuilder buildSdkExceptionMessage(final String notice, final SdkException e) {
        final ExceptionMessageBuilder messageBuilder = new ExceptionMessageBuilder();
        messageBuilder.addNotice(notice);
        messageBuilder.addNotice("Caught an SdkException.");
        messageBuilder.addItem("Error Message");
        messageBuilder.addElement(e.getMessage());

        return messageBuilder;
    }

    // -----------------------------------------------------
    //                                          For Exception
    //                                          -------------
    /**
     * AWS SDK以外で例外が発生したときの処理
     * @param notice 通知情報 (NotNull)
     * @param e Amazon Client例外 (NotNull)
     * @return 例外メッセージビルダ　(NotNull)
     */
    default ExceptionMessageBuilder buildExceptionMessage(final String notice, final Exception e) {
        final ExceptionMessageBuilder messageBuilder = new ExceptionMessageBuilder();
        messageBuilder.addNotice(notice);
        messageBuilder.addItem("Error Message");
        messageBuilder.addElement(e.getMessage());

        return messageBuilder;
    }
}