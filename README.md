#Todoアプリケーション

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

 | カラム名 | 型 | null | key |
 |:-----------|:------------|:------------|:--- |
 | id       |INT BIGINT|     NO    | primary key |
 | title    |VARCHAR(100)|    NO    |  |
 | price       |INT|     NO     |  |
 | description         |   VARCHAR(500) |      NO      |  |
 | image_path       |       VARCHAR(500) |    YES    |  |


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
│   │   │               │   ├── controller
│   │   │               │   │   ├── item
│   │   │               │   │   │   └── ItemsController.java
│   │   │               │   │   └── oauth
│   │   │               │   │       └── GithubOauthController.java
│   │   │               │   └── exception
│   │   │               │       ├── ErrorResponse.java
│   │   │               │       ├── ImageNotFoundException.java
│   │   │               │       ├── ImageNotUploadedException.java
│   │   │               │       ├── InvalidAuthorizeException.java
│   │   │               │       ├── InvalidImageFileException.java
│   │   │               │       ├── ItemExceptionHandler.java
│   │   │               │       ├── ItemNotFoundException.java
│   │   │               │       ├── OauthExceptionHandler.java
│   │   │               │       └── SearchResultNotFoundException.java
│   │   │               │       
│   │   │               ├── domain
│   │   │               │
│   │   │               │   ├── repository
│   │   │               │   │   └── ItemRepository.java
│   │   │               │   └── service
│   │   │               │       ├── item
│   │   │               │       │   └── ItemService.java
│   │   │               │       └── oauth
│   │   │               │           └── GithubOauthService.java
│   │   │               └── infrastructure
│   │   │                   └── entity
│   │   │                       └── Item.java
│   │   └── resources
│   │       ├── application.yml
│   │       ├── data.sql
│   │       ├── static
│   │       │   ├── css
│   │       │   │   ├── bootstrap.css
│   │       │   │   └── custom.css
│   │       │   ├── images
│   │       │   │   └── 1_f9f29f63-c5fd-4959-8557-42691fe4355c.jpeg
│   │       │   └── js
│   │       │       ├── bootstrap.js
│   │       │       └── jquery.js
│   │       └── templates
│   │           ├── error
│   │           │   ├── 401.html
│   │           │   ├── 404.html
│   │           │   └── 500.html
│   │           └── oauth
│   │               ├── github
│   │               │   └── index.html
│   │               └── login.html
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── kawasakirestapi
│                       └── KawasakiRestapiApplicationTests.java
└── swagger.yml
```
各層の役割
```
・アプリケーション層
UI
ドメイン層のオブジェクトを使って、アプリケーションの機能を実現する層。
クライアントとの入出力とビジネスロジックをつなぐ
・ドメイン層
ビジネスロジック
ドメインを表現するオブジェクト
・インフラストラクチャ層
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
・Githubアカウント登録
・Githubで取得したclientidとsecretをIntellijの環境変数の登録
・アプリ起動
```
./gradle bootRun
```

