Luxa Harbor
=======================
[LastaFluteのサンプルプルジェクト](https://github.com/lastaflute/lastaflute)からフォーク
AWS ECSのコンテナ上で動くサービスとして修正.
- ビルドツールをMavenからGradleに移行
- ログの出力先を標準出力に修正
- データベースの接続情報をSecurity Managerから取得する.


# Quick Trial
Can boot it by example of LastaFlute:

1. git clone https://github.com/luxa/lastaflute-example-harbor.git
2. prepare database by *ReplaceSchema at DBFlute client directory 'dbflute_maihamadb'
3. compile it by Java8, on e.g. Eclipse ... as Gradle project of execute "gradle -x test build"
4. build docker image, execute "gradle buildDockerImage"
5. run docker image, execute "gradle runDocker"
6. access to http://localhost:8090/harbor
and login by user 'Pixy' and password 'sea', and can see debug log at console.

*ReplaceSchema
```java
// call manage.sh at lastaflute-example-harbor/dbflute_maihamadb
// and select replace-schema in displayed menu
...:dbflute_maihamadb ...$ sh manage.sh
```

*main() method
```java
public class HarborBoot {

    public static void main(String[] args) {
        new JettyBoot(8090, "/harbor").asDevelopment(isNoneEnv()).bootAwait();
    }
}
```

# Build Image
TODO
- Java8環境のイメージからTomcatのインストール,アプリをコピーしているが、Tomcatのイメージを作成し、そのイメージにアプリをこぴーするように変更
- アプリケーションのログは標準出力に変更したが、Tomcatのログ(catalina.out,gc.log等)も標準出力に変更する.

# Information
## License
Apache License 2.0
