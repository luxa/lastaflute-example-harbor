package org.docksidestage.mylasta.direction.sponsor;

import org.apache.commons.lang3.StringUtils;
import org.docksidestage.app.logic.aws.secretsmanager.DefaultSecretsManagerClient;
import org.docksidestage.mylasta.direction.HarborConfig;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.set.ImmutableSet;
import org.lastaflute.core.direction.PropertyFilter;
import org.lastaflute.core.util.ContainerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Harbor Property Filter.<br>
 * <pre>
 * 開発環境以外のJDBC_USER/JDBC_PASSWORDをSecrets Mangerから取得する.
 * Secrets Managerから値を取り出すSecretIdは、"${Lasta.env}.${propertyValue}"とする.
 * propertyKeyを使用するとマルチプロジェクトの際に、サービス毎に値を変更するのができないのでpropertyValueを使用する.
 * もしくはVersion-Stageをつかって、同一キーに複数の値を割り当てるようにする
 * 実際にどうするかは後で決める.
 * ※
 * 開発環境で試す場合、LocalStackのSecretsManagerを有効にし以下のコマンドを実行しておくこと
 * (本来JDBC_USER/JDBC_PASSWORDで値が変わるが、developのJDBC_USER/JDBC_PASSWORDは同一値なので登録は１回だけ)
 * > awslocal secretsmanager put-secret-value --secret-id .maihamadb --secret-string maihamadb --region ap-northeast-1
 * </pre>
 * @author harada
 */
public class HarborPropertyFilter implements PropertyFilter {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final Logger logger = LoggerFactory.getLogger(HarborPropertyFilter.class);
    private static final ImmutableSet<String> SECRETS_KEYS = Sets.immutable.of(HarborConfig.JDBC_USER, HarborConfig.JDBC_PASSWORD);

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // LastaFluteが初期化中に呼ばれるのでDIコンテナから取り出して設定するので@Resourceは書かない
    private HarborConfig config;
    private DefaultSecretsManagerClient secretManagerClient;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    /**
     * コンストラクタ.
     * HarborPropertyFilterが依存しているコンポーネントを初期化する.
     * AssistantDirectorでインスタンスを生成するのでDIを自分で行う.
     */
    public HarborPropertyFilter() {
        this.config = ContainerUtil.getComponent(HarborConfig.class);
        this.secretManagerClient = ContainerUtil.getComponent(DefaultSecretsManagerClient.class);
    }

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Override
    public String filter(final String propertyKey, final String propertyValue) {
        return needsSecretsManagerValue(propertyKey) ? getSecreManagerValue(propertyKey, propertyValue) : propertyValue;
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    protected boolean needsSecretsManagerValue(final String propertyKey) {
        return SECRETS_KEYS.contains(propertyKey);
    }

    protected String getSecreManagerValue(final String propertyKey, final String propertyValue) {
        final String value = config.isDevelopmentHere() ? propertyValue : secretManagerClient.getSecretValue(getSecretId(propertyValue));
        logger.debug("Read property from secretsmanager. propertKey:{}, propertyValue:{}, secretsValue:{}", propertyKey, propertyValue,
                value);

        return value;
    }

    protected String getSecretId(final String propertyValue) {
        return StringUtils.defaultString(System.getProperty("lasta.env")) + "." + propertyValue;
    }
}
