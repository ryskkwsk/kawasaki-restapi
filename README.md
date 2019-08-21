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
 | id       |BIGINT(20)|     NO    | primary key |
 | title    |VARCHAR(100)|    NO    |  |
 | price       |INT(20)|     NO     |  |
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
│   │   │               ├── controller
│   │   │               │   └── ItemsController.java
│   │   │               ├── entity
│   │   │               │   └── Item.java
│   │   │               ├── exception
│   │   │               │   ├── ErrorResponse.java
│   │   │               │   ├── ImageNotFoundException.java
│   │   │               │   ├── ImageNotUploadedException.java
│   │   │               │   ├── InvalidImageFileException.java
│   │   │               │   └── ItemNotFoundException.java
│   │   │               ├── handler
│   │   │               │   └── ItemExceptionHandler.java
│   │   │               ├── repository
│   │   │               │   └── ItemRepository.java
│   │   │               └── service
│   │   │                   └── ItemService.java
│   │   └── resources
│   │       ├── application.yml
│   │       ├── data.sql
│   │       ├── static
│   │       │   └── images
│   │       │       └── 1_2019082018505423.jpeg
│   │       └── templates
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── kawasakirestapi
│                       └── KawasakiRestapiApplicationTests.java
└── swagger.yml
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
・アプリ起動
```
./gradle bootRun
```

