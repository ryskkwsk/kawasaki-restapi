#RestfulなAPIの商品管理アプリケーション

###使用技術
-Spring Boot2.1.7

-Java8

-MySQL5.7

###全体の設計・構成についての説明

####ルーティング

 | HTTPメソッド | URL | 概要 |
 |:-----------|:------|:--- |
 | GET       |        api/items         | 商品情報の取得 |
 | POST    |      api/items        | 商品情報の保存 |
 | DELETE       |        api/items/{id}          | 商品情報の削除 |
 | DELETE         |   api/items/image/{id}            | 商品画像の削除 |
 | PUT       |       api/items/{id}         | 商品情報の編集 |
 | POST    |     api/items/image/{id}        | 商品画像の登録 |
 | GET | api/items/image/{id}   | 商品画像の表示　|
 | GET | api/items/search   | 商品情報の検索 |


####DB設計

#####items
 | カラム名 | 型 | null | key |
 |:-----------|:------------|:------------|:--- |
 | id       |INT BIGINT|     NO    | primary key |
 | title    |VARCHAR(100)|    NO    |  |
 | price       |INT|     NO     |  |
 | description         |   VARCHAR(500) |      NO      |  |
 | image_path       |       VARCHAR(500) |    YES    |  |
 
#####AutenticationToken
  | カラム名 | 型 | null | key |
  |:-----------|:------------|:------------|:--- |
  | id       |INT BIGINT|     NO    | primary key |
  | dead_line    |DATETIME|    YES   |  |
  | create_at      |DATETIME|     YES     |  |
  | auth_token         |   VARCHAR(255) |      YES      |  |
  | user_id       |       INT BIGINT |    YES    |  |
  | user_name       |       VARCHAR |    YES    |  |
  
#####access_log
  | カラム名 | 型 | null | key |
  |:-----------|:------------|:------------|:--- |
  | id       |INT BIGINT|     NO    | primary key |
  | access_count    |INT|    YES   |  |
  | aggregation_date      |DATE|     YES     |  |
  | request_mehod         |   VARCHAR(255) |      YES      |  |
  | request_url       |       VARCHAR(255) |    YES    |  |
  | responsetimes       |       INT |    YES    |  |
  | status_code       |       INT |    YES    |  |


####ディレクトリ構成
```
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── kawasakirestapi
│   │   │               ├── KawasakiRestapiApplication.java
│   │   │               ├── application
│   │   │               │   ├── controller-
│   │   │               │   └── exception-
│   │   │               │   └── filter
│   │   │               │   └── inetrceptor
│   │   │               │       
│   │   │               ├── domain
│   │   │               │   ├── repository-
│   │   │               │   └── service-
│   │   │               │   └── setting
│   │   │               │   └── batch
│   │   │               │   └── config
│   │   │               │   └── dto
│   │   │               │             
│   │   │               └── infrastructure
│   │   │                   └── entity-
│   │   └── resources
│   │       ├── application.yml
│   │       ├── data.sql
│   │       ├── static
│   │       └── templates
│   │       └── logback-spring.xml
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


    
####開発環境のセットアップ
・Java8 インストール
```
$ brew cask install java8 
$ export JAVA_HOME=`/usr/libexec/java_home -v 1.8`
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
```

#####環境変数の設定
作成したClient IDとClient Secretを環境変数に設定する。

```
Intelli IDEAの場合
「option」+「command」+「r」を押下する。
「Edit Configurations...」を選択する。
「VM options」に -DOAUTHAPP_GITHUB_CLIENT_ID=xxx -DOAUTHAPP_GITHUB_CLIENT_SECRET=yyyを入力して、xxxをClient IDに差し替え、yyyをClient Secretに差し替える。
application.ymlに上記で設定した環境変数を設定する
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

