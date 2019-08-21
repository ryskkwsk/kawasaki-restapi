#Todoアプリケーション

###使用技術
-Spring Boot2.1.7

-Java8

-MySQL5.7

###全体の設計・構成についての説明

####ルーティング

 | HTTPメソッド | URL | Conrtollerメソッド | 概要 |
 |:-----------|:------------|:------------|:--- |
 | GET       |        v1/items |     getItems     | 商品情報の取得 |
 | POST    |      v1/items |    createItem    | 商品情報の保存 |
 | DELETE       |        v1/items/{id} |     deleteItem     | 商品情報の削除 |
 | DELETE         |   v1/items/image/{id} |      deleteItemImage      | 商品画像の削除 |
 | PUT       |       v1/items/{id} |    editItem    | 商品情報の編集 |
 | POST    |     v1/items/image/{id} |   uploadImageItem    | 商品画像の登録 |
 | GET | v1/items/image/{id} | showImageItem | 商品画像の表示　|
 | POST | v1/items/search | searchItems | 商品情報の検索 |


####DB設計

 | カラム名 | 型 | null | key |
 |:-----------|:------------|:------------|:--- |
 | id       |BIGINT(20)|     NO    | primary key |
 | title    |VARCHAR(100)|    NO    |  |
 | price       |INT(20)|     NO     |  |
 | description         |   VARCHAR(500) |      NO      |  |
 | image       |       VARCHAR(255) |    YES    |  |


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
│   │   │               │   └── ItemRestController.java
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
│   │       ├── application.properties
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
・application.ymlの作成
```
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/kawasaki_restfulapi
    username: root
    password:
  jpa:
    database: MYSQL
    hibernate:
      ddl-auto: update
    database-platform: org.hibernate.dialect.MySQL5InnoDBDialect
  mvc:
  throw-exception-if-no-handler-found: true
  resources:
  add-mappings: false
  localImagesPath: src/main/resources/static/images

```
・アプリ起動
```
./gradle bootRun
```

