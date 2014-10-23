# Finatra AngularJS Example Application - ShoppingCart

[![Build Status](https://travis-ci.org/fosco-maestro/finatra-angular-example.svg)](https://travis-ci.org/fosco-maestro/finatra-angular-example)

This example using [Finatra](http://finatra.info/) for the backend, [H2](http://www.h2database.com/) as data storage and 
[AngularJS](https://angularjs.org/) + [Bootstrap](http://getbootstrap.com/) for the frontend as Single Page Application.

Finatra requires either [maven](http://maven.apache.org/) or [sbt](http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html) to build and run your app.

## Setup

### Bower Instructions

For managing dependencies for the frontend application using [Bower](http://bower.io/).

Before running application please install frontend dependencies by using following command:

```
$ bower install
```

### SBT Instructions

#### Runs your app on port 7070

Ti run application use following command:

```
$ sbt run
```

It will start on port 7070, so you should see result on: [http://localhost:7070](http://localhost:7070)

#### Testing

You can run tests:

```
$ sbt test
```

## Application API

Application contain public API and private that requires authentication first.

You can use [cURL](http://curl.haxx.se/) to test application.

### Public API

#### /api/user

You can get authentication token for your communication with server's private API by querying following URL:

```
$ curl http://localhost:7070/api/user/authentication
```

You can also can check list of authenticated users by following URL (this part made only for debugging):

```
$ curl http://localhost:7070/api/user
```

#### /api/product

On following URL you can get list of available products:
 
```
curl http://localhost:7070/api/product
```

### Private API

For this part of API you need to have authentication toking provided by ```/api/user/authentication```

#### /api/cart

You can get list of all products currently in your basket:

```
$ curl -X GET -G http://localhost:7070/api/cart -d token={token}
```

Add new product to your basket:

```
$ curl -X PUT http://localhost:7070/api/cart -d productId={product_id} -d token={token}
```

**Please note** that by adding the same product multiple time into your basket, you will just increase it quantity.

You can update quantity of product that is already in your basket:

```
$ curl -X POST http://localhost:7070/api/cart -d productId={product_id} -d token={token} -d quantity={quantity.?}
```

**Please note** that parameter ```quantity``` is optional, you can set persist amount of product by specifying 
it or just leave it empty and quantity of product will be increased by 1. 

You can remove product from your cart using following URL:

```
$ curl -X DELETE -G http://localhost:7070/api/cart -d token={token} -d productId={product_id}
```

## Known Issues

```$ sbt assembly``` will hangup with an error:

```
[error] (*:assembly) deduplicate: different file contents found in the following:
[error] public/index.html
[error] /Users/pavlo/.ivy2/cache/com.twitter/finatra_2.10/jars/finatra_2.10-1.5.2.jar:public/index.html
[error] Total time: 7 s, completed Oct 23, 2014 12:26:51 PM
```
Issue reported in following ticket: [https://github.com/twitter/finatra/issues/133](https://github.com/twitter/finatra/issues/133)

Second one is warning durring tests ```$ sbt test```

```
[warn] /Users/pavlo/Projects/shopping-cart/src/test/scala/com/acme/ShoppingCart/AppSpec.scala:71: non-variable type argument String in type pattern scala.collection.Map[String,String] is unchecked since it is eliminated by erasure
[warn]       case Some(map: Map[String, String]) => map
[warn]                      ^
[warn] one warning found
```

## Copyright

Copyright (C) 2014 Pavlo Voznenko.

Distributed under the MIT License.
