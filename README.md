#RestfulなAPIの商品管理アプリケーション

###使用技術
-Spring Boot2.1.7

-Java8

-MySQL5.7

###全体の設計・構成についての説明

####APIルーティング

 | HTTPメソッド | URL | 概要 |
 |:-----------|:------|:--- |
 | GET       |      api/items            | 商品情報の取得 |
 | POST      |      api/items            | 商品情報の保存 |
 | DELETE    |      api/items/{id}       | 商品情報の削除 |
 | DELETE    |      api/items/image/{id} | 商品画像の削除 |
 | PUT       |      api/items/{id}       | 商品情報の編集 |
 | POST      |      api/items/image/{id} | 商品画像の登録 |
 | GET       |      api/items/image/{id} | 商品画像の表示　|
 | GET       |      api/items/search     | 商品情報の検索 |

####Oauth認証ルーティング
 | HTTPメソッド | URL | 概要 |
 |:-----------|:------|:--- |
 | GET       |     github/login           | Githubログイン実行 　　　　　 |
 | GET       |     github/callback        | Github callback実行 　　 　 |
 | GET       |     github/profile         | Githubのプロフィール画面表示  |
 | GET       |     github/logout          | ログアウト実行 |


####DB設計

#####items

 | カラム名 | 型 | null | key |
 |:-----------|:------------|:------------|:--- |
 | id           | INT BIGINT   |    NO    | primary key |
 | title        | VARCHAR(100) |    NO    |             |
 | price        | INT          |    NO    |             |
 | description  | VARCHAR(500) |    NO    |             |
 | image_path   | VARCHAR(500) |    YES   |             |
 
#####AutenticationToken

 | カラム名 | 型 | null | key |
 |:-----------|:------------|:------------|:--- |
 | id         |  INT BIGINT   |    NO    | primary key |
 | dead_line  |  DATETIME     |    YES   |             |
 | create_at  |  DATETIME     |    YES   |             |
 | auth_token |  VARCHAR(255) |    YES   |             |
 | user_id    |  INT BIGINT   |    YES   |             |
 | user_name  |  VARCHAR      |    YES   |             |
  
#####access_log

 | カラム名 | 型 | null | key |
 |:-----------|:------------|:------------|:--- |
 | id                |  INT BIGINT   |    NO    | primary key |
 | access_count      |  INT          |    YES   |             |
 | aggregation_date  |  DATE         |    YES   |             |
 | request_mehod     |  VARCHAR(255) |    YES   |             |
 | request_url       |  VARCHAR(255) |    YES   |             |
 | responsetimes     |  INT          |    YES   |             | 
 | status_code       |  INT          |    YES   |             |


####ディレクトリ構成

```
├── src
│   ├── batch
│   │   └── image
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── kawasakirestapi
│   │   │               ├── KawasakiRestapiApplication.java
│   │   │               ├── application
│   │   │               │   ├── controller
│   │   │               │   │   ├── item
│   │   │               │   │   ├── log
│   │   │               │   │   ├── oauth
│   │   │               │   │   └── sessioninfo
│   │   │               │   ├── exception
│   │   │               │   │   ├── Item
│   │   │               │   │   └── oauth
│   │   │               │   ├── filter
│   │   │               │   └── interceptor
│   │   │               ├── domain
│   │   │               │   ├── batch
│   │   │               │   ├── config
│   │   │               │   ├── dto
│   │   │               │   ├── form
│   │   │               │   ├── repository
│   │   │               │   │   ├── item
│   │   │               │   │   ├── log
│   │   │               │   │   └── oauth
│   │   │               │   ├── service
│   │   │               │   │   ├── aws
│   │   │               │   │   ├── item
│   │   │               │   │   ├── log
│   │   │               │   │   └── oauth
│   │   │               │   └── setting
│   │   │               └── infrastructure
│   │   │                   └── entity
│   │   │                       ├── item
│   │   │                       │   └── Item.java
│   │   │                       ├── log
│   │   │                       │   └── AccessLog.java
│   │   │                       └── oauth
│   │   │                           └── AuthenticationToken.java
│   │   └── resources
│   │       ├── static
│   │       │   ├── css
│   │       │   │   └── custom.css
│   │       │   └── images
│   │       └── templates
│   │           ├── error
│   │           │   ├── 401.html
│   │           │   ├── 404.html
│   │           │   └── 500.html
│   │           ├── log
│   │           │   ├── loglist.html
│   │           │   └── searchLogList.html
│   │           └── oauth
│   │               ├── github
│   │               │   └── profile.html
│   │               └── login.html
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── kawasakirestapi
│                       └── KawasakiRestapiApplicationTests.java
└── swagger.yml

```
各ディレクトリの役割
```
・application配下
UI
ドメイン層のオブジェクトを使って、アプリケーションの機能を実現する層。
クライアントとの入出力とビジネスロジックをつなぐ
・domain配下
ビジネスロジック
ドメインを表現するオブジェクト
・infrastructure配下
各層を実装するための具体的な技術要素を提供する層
永続化の実装
他サービスとの通信等の実装
```

## 本番環境

商品管理アプリケーション
https://kawasakiryosuke.com/

#### VPC
|Name       |CIDR           |
|-----------|---------------|
|VPC-Shared2|10.10.0.0/16 |

#### サブネット
|Name                |IPv4 CIDRv        |アベイラビリティーゾーン|
|:--------------------:|:------------------:|:--------------------:|
|kawasakiryosuke-public-1a  |10.10.12.0/27   |ap-northeast-1a     |
|kawasakiryosuke-public-1c  |10.10.12.32/27  |ap-northeast-1c     |
|kawasakiryosuke-private-1a |10.10.12.128/27 |ap-northeast-1a     |
|kawasakiryosuke-private-1c |10.10.12.160/27 |ap-northeast-1c     |

#### EC2
| Name           | サブネット         | インスタンスタイプ | セキュリティグループ | OS |
|:--------------:|:------------------:|:------------------:|:--------------------:|:--------------------:|
| kawasakiryosuke-ec2-pub-1a | kawasakiryosuke-public-1a　| t2.micro         | kawasakiryosuke-ec2-scg　     | Amazon Linux2　     |
| kawasakiryosuke-ec2-pub-1c | kawasakiryosuke-public-1c| t2.micro           | kawasakiryosuke-ec2-scg　　 | Amazon Linux2　     |

#### RDS
| Name         | サブネット                            | エンジン        | セキュリティグループ | インスタンスタイプ |
|:------------:|:-------------------------------------:|:---------------:|:--------------------:|:------------------:|
| kawasakiryosuke-db   |kawasakiryosuke-private-1a, kawasakiryosuke-private-1c | MySQL Community | kawasakiryosuke-db-scg　    | t2.micro           |

#### S3
| Name          | 用途  |
|:-------------:|:---------------:|
| kawasakiryosuke-front | React SPAの配置 |
| kawasakiryosuke-deploy | Appデプロイ成果物を配置　　  |
| kawasakiryosuke-image  | 画像の配置 |
| kawasakiryosuke-log | ログ |

#### ALB
| Name         | Target(Port)       |AvailabilityZone                       | セキュリティグループ  |
|:------------:|:------------------:|:-------------------------------------:|:---------------------:|
| kawasakiryosuke-alb  | tg-alb-kawasakiryosuke(http:80, https:443) | kawasakiryosuke-public-1a, kawasakiryosuke-public-1c  | kawasakiryosuke-alb   |

#### CloudFront
| Path         | Origin     |
|:------------:|:----------:|
| /github/*    | ELB-kawasakiryosuke-alb-728460785 |
| /api/*       | ELB-kawasakiryosuke-alb-728460785 |
| /token/*     | S3-kawasakiryosuke-front          |
| /loglist/*   | ELB-kawasakiryosuke-alb-728460785 |
| /login*      | S3-kawasakiryosuke-front |
| Default(*)   | S3-kawasakiryosuke-front |

#### CodeBuild
| Name | ソースプロバイダ | リポジトリ |
|:------------:|:----------:|:---------:|
| kawasakiryosuke-restfulapi-build | AWS CodePipelinde | kawasaki-restapi |
| kawasakiryosuke-frontend-build | AWS CodePipelinde | kawasaki-react |

#### CodeDeploy
| Name | グループ | プラットフォーム |
|:------------:|:----------:|:----------:|
| kawasakiryosuke-restapi-build | kawasakiryosuke-restapi-deploy-group | EC2/オンプレミス |

#### CodePipeline
| Name | 
|:----:|
|kawasakiryosuke-restapi-pipeline |
|kawasakiryosuke-frontend-pipeline |

#### AWSのCodeDeployでエラー
```
・CodeDeployAgentをEC2にインストール
sudo yum -y install ruby; yum -y install wget; cd /home/ec2-user; wget https://aws-codedeploy-ap-northeast-1.s3.amazonaws.com/latest/install; chmod +x ./install; sudo ./install auto
・/var/log/aws/codedeploy-agent/　ログを確認

・キャッシュの削除
/opt/codedeploy-agent/deployment-root/deployment-instructions/ 配下のファイルを削除
```

#### EC2で環境構築
```
・それぞれEC2にjavaをインストールする。
sudo yum install java-11-amazon-corretto
・javaがインストールされたか確認
java -version 
・アンインストールする場合
sudo yum remove java-11-amazon-corretto

・mariaDB削除
$ sudo yum remove mariadb-libs
$ sudo rm -rf /var/lib/mysql

・mysqlインストール
$ sudo yum install mysql

・RDS
マスター名:admin_kawasaki
パスワード:kawayan1111

・EC2でrdsへの接続
mysql -h kawasakiryosuke-db.c35xcnhiknum.ap-northeast-1.rds.amazonaws.com -P 3306 -u admin_kawasaki -p

・データベースの作成
MySQL [(none)]> create database kawasaki_restfulapi;
```

    
####開発環境のセットアップ
・SDKMANでJava11インストール
```
・SDKMANのインストール
$ curl -s "https://get.sdkman.io" | bash
$ source "$HOME/.sdkman/bin/sdkman-init.sh"

・AdoptOpenJDK の 11.xx.xx.hs-adpt(xxはマイナーバージョン)を探す
$ sdk list java

・インストール
$ sdk install java 11.xx.xx.hs-adpt

・デフォルトで使用するバージョンを固定する
$ sdk default java 11.0.4.hs-adpt

・javaのバージョンを確認
$ java -version
```

・MySQLインストール
```
$ brew install mysql@5.7
```
・MySQL起動
```
$ mysql.server start
```
・DBの作成
```
 $ mysql -u root #MySQLにログイン
 mysql> CREATE DATABASE kawasaki_restfulapi; #データベース作成
```
####Github認証情報の環境変数の設定
ローカル環境での動作確認のために、Githubの認証情報を環境変数に設定をする。

#####GitHubのOAuthアプリケーションの登録
```
1. Githubのアカウントが無ければ登録
2. Githubページにログインする。
3. 右上のアイコンを押す
4. Setting>Developer settings>OAuth Appsを押す
5. 右上のNew OAuth Appを押す
6. 各項目を入力し、Register applicationを押す
7. 作成したアプリケーションのページに表示されたClientID とSecretを保持しておく。
8. 本番環境の際、Homepage URLとAuthorization callbackURLを本番用に書き換える
```

#####環境変数の設定
作成したClient IDとClient Secretを環境変数に設定する。

```
Intelli IDEAの場合
「option」+「command」+「r」を押下する。
「Edit Configurations...」を選択する。
「VM options」に -DOAUTHAPP_GITHUB_CLIENT_ID=xxx -DOAUTHAPP_GITHUB_CLIENT_SECRET=yyyを入力して、xxxをClient IDに差し替え、yyyをClient Secretに差し替える。
application.ymlに上記で設定した環境変数を設定する
また、application-local.ymlを使用しているため、「VM options」-Dspring.profiles.active=localを追加
```

####APIのアクセス認証
```
・APIリクエストのheaderに認証情報を追加
  Key: Authorization
  Value: Bearer ***********(取得したいアクセストークン)
```

####バッチ処理
```
1.APIにアクセスがあった日毎にlogs/access内にログファイルが作成され、同日中のアクセスは全てそのログファイルに記録される。
2.毎日AM10:00にlogs/access内の前日のlogファイルを集計する。
3.ログを１行ずつ読み取り、HTTPMethod, URL, HTTPStatus が重複するログに関してはアクセス回数と処理時間の平均のみを記録する。
4.ログの一覧を取得するにはAuth認証後、http://localhost:8080/loglist にアクセスしてログボタンを押してください。
```
バッチ処理の仕様は/src/batch/image配下のバッチ処理フロー図、概要図を参照してください。

・アプリ起動
```
./gradle bootRun
```

