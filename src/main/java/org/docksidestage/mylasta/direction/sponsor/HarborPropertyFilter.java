package org.docksidestage.mylasta.direction.sponsor;

import org.apache.commons.lang3.StringUtils;
import org.docksidestage.app.logic.aws.secretsmanager.DefaultSecretsManagerClient;
import org.docksidestage.mylasta.direction.HarborConfig;
import org.lastaflute.core.direction.PropertyFilter;
import org.lastaflute.core.util.ContainerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Harbor Property Filter.<br>
 * <pre>
 * プロパティの値が"screts.value:site"のプロパティをSecrets Mangerから取得する.
 * Secrets Managerから値を取り出すSecretIdは、"${Lasta.env}:${propertykey}:${site}"とする.
 * propertyKeyを使用するとマルチプロジェクトの際に、サービス毎に値を変更するのができないのでpropertyValueを使用する.
 * もしくはVersion-Stageをつかって、同一キーに複数の値を割り当てるようにする
 * 実際にどうするかは後で決める.
 * ※
 * 開発環境で試す場合、LocalStackのSecretsManagerを有効にし以下のコマンドを実行しておくこと
 * jdbc.user     = secrets.value
 * jdbc.password = secrets.value
 * > awslocal secretsmanager put-secret-value --secret-id integration:jdbc.user             --secret-string maihamadb --region ap-northeast-1
 * > awslocal secretsmanager put-secret-value --secret-id integration:jdbc.password         --secret-string maihamadb --region ap-northeast-1
 * サイト別にしたい場合は以下のようにする
 * jdbc.user     = secrets.value:harbor
 * jdbc.password = secrets.value:harbor
 * > awslocal secretsmanager put-secret-value --secret-id integration:jdbc.user:harbor     --secret-string maihamadb --region ap-northeast-1
 * > awslocal secretsmanager put-secret-value --secret-id integration:jdbc.password:harbor --secret-string maihamadb --region ap-northeast-1
 * </pre>
 * @author harada
 */
public class HarborPropertyFilter implements PropertyFilter {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final Logger logger = LoggerFactory.getLogger(HarborPropertyFilter.class);
    private static final String REPLACEMENT_VALUE = "secrets.value";
    private static final String SEPARETOR = ":";

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
        return needsSecretsManagerValue(propertyValue) ? getSecreManagerValue(propertyKey, propertyValue) : propertyValue;
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    protected boolean needsSecretsManagerValue(final String propertyValue) {
        if (propertyValue == null) {
            return false;
        }
        return propertyValue.startsWith(REPLACEMENT_VALUE);
    }

    protected String getSecreManagerValue(final String propertyKey, final String propertyValue) {
        final String value = secretManagerClient.getSecretValue(getSecretId(propertyKey, propertyValue));
        logger.debug("Read property from secretsmanager. propertKey:[{}], propertyValue:[{}], secretsValue:[{}]", propertyKey,
                propertyValue, value);

        return value;
    }

    protected String getSecretId(final String propertyKey, final String propertyValue) {
        final StringBuilder sb =
                new StringBuilder(StringUtils.defaultString(System.getProperty("lasta.env"))).append(SEPARETOR).append(propertyKey);
        final String[] values = propertyValue.split(SEPARETOR);
        if (values.length == 2) {
            sb.append(SEPARETOR).append(values[1]);
        }
        return sb.toString();
    }
}
