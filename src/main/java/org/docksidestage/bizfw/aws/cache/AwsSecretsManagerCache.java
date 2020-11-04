package org.docksidestage.bizfw.aws.cache;

import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.map.MutableMap;

/**
 * AWS Secret Manager Cacheクラス.
 * appパッケージはprototypeの為、bizパッケージにキャッシュを置く. (bizパッケージはシングルトン)
 * 一応キャッシュはするが頻繁に読み込む値ではないのでget/setの排他制御をしない
 * @author harada
 */
public class AwsSecretsManagerCache {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private static final MutableMap<String, String> CACHE_MAP = Maps.mutable.empty();

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // -----------------------------------------------------
    //                                       Get cache value
    //                                       ---------------
    /**
     * キャッシュされている値を返す.
     * @param secretId  シークレットID (NotNull)
     * @return シークレット値 (NullAllowed)
     */
    public String get(String secretId) {
        return CACHE_MAP.get(secretId);
    }

    // -----------------------------------------------------
    //                                       Set cache value
    //                                       ---------------
    /**
     * Secret Managerから取得した値をキャッシュする.
     * @param secretId  シークレットID (NotNull)
     * @param シークレット値 (NullNull)
     */
    public void set(String secretId, String secretValue) {
        CACHE_MAP.put(secretId, secretValue);
    }
}
