# Finatra AngularJS REST API Example Application - ShoppingCart

[![Build Status](https://travis-ci.org/pvoznenko/finatra-angular-example.svg?branch=master)](https://travis-ci.org/pvoznenko/finatra-angular-example)

Main goal of this project to show how you could use [Finatra](http://finatra.info/).

This example using [Finatra](http://finatra.info/) for the backend and [Slick](http://slick.typesafe.com/) for work with [H2](http://www.h2database.com/) that used as data storage. 
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

It will start on port `7070`, so you should see result on: [http://localhost:7070](http://localhost:7070)

#### Testing

You can run tests:

```
$ sbt test
```

## Application API

Application contain public API and private that requires authentication first.

You can use [cURL](http://curl.haxx.se/) to test application.

API response in JSON format.

Supported request types:

* `application/json`

If you will asked for another type, server will response with code `406` and message `No matching accepted Response 
format could be determined!`.

### Version

You can configure version of your API at `resources/application.conf`.

Make sure that your requests contain correct version of API.

Default version is set to `3`.

### Public API

#### /api/v3/users

You can get authentication token for your communication with server's private API by querying following URL:

```
$ curl -i -H Accept:application/json -X POST http://localhost:7070/api/v3/users/authentication
```

You will get response status `201` - because this method will fake user creation and provide you with auth token for private API.

You can also can check list of authenticated users by following URL (this part made only for debugging, limit is 
optional parameter, default 10. If limit < 0 then you will get all data):

```
$ curl -i -H Accept:application/json -X GET -G http://localhost:7070/api/v3/users -d limit={limit.?}
```

You should get response code `200`.

#### /api/v3/products

On following URL you can get list of available products (limit is optional parameter, default 10. 
If limit < 0 then you will get all data):
 
```
curl -i -H Accept:application/json -X GET -G http://localhost:7070/api/v3/products -d limit={limit.?}
```

You should get response code `200`.

### Private API

For this part of API you need to have authentication toking provided by `/api/v3/users/authentication`.
You should pass token in header with key `token`.

#### /api/v3/cart/products

You can get list of all products currently in your basket:

```
$ curl -i -H Accept:application/json -X GET -G http://localhost:7070/api/v3/cart/products -H token:{token}
```

* If everything is OK you should get response code `200`.

##### Create

Add new product to your basket:

```
$ curl -i -H Accept:application/json -X PUT http://localhost:7070/api/v3/cart/products/{productId} -H token:{token}
```

* If everything is OK you should get response code `201`;
* If you trying to add product that is already in your cart you will get response code `409` with following 
message `Products is already in user's cart!`;
* Parameter `productId` should be integer otherwise you will get response with code `422` and message `Illegal Argument!`;
* Parameter `productId` should be existing product in the market otherwise you will get response with code `404` and 
message `Product with provided id '{productId}' is not exist!`.

##### Update

You can update quantity of product that is already in your basket:

```
$ curl -i -H Accept:application/json -X PUT http://localhost:7070/api/v3/cart/products/{productId}/quantity/{quantity} -H token:{token}
```

* If everything is OK you should get response code `204`;
* Parameter `productId` should be integer otherwise you will get response with code `422` and message `Illegal Argument!`;
* Parameter `productId` should be existing product in the market otherwise you will get response with code `404` and 
message `Product with provided id '{productId}' is not exist!`;
* Parameter `quantity` should be positive integer otherwise you will get response with code `422` and message `Illegal Argument!`;
* If you trying to update product that is not in user's shopping cart you will get response code `404` with following 
message `Product should be in user's cart!`.

##### Delete

You can remove product from your cart using following URL:

```
$ curl -i -H Accept:application/json -X DELETE -G http://localhost:7070/api/v3/cart/products/{productId} -H token:{token}
```

* If everything is OK you should get response code `204`;
* Parameter `productId` should be integer otherwise you will get response with code `422` and message `Illegal Argument!`;
* Parameter `productId` should be existing product in the market otherwise you will get response with code `404` and 
message `Product with provided id '{productId}' is not exist!`;
* If you trying to remove product that is not in user's shopping cart you will get response code `404` with following 
message `trying to remove product from user's shopping cart that is not there!`.

### Exceptions

* For `Private API` if parameter `token` is missing you will get response with code `401` and message `Parameter 'token' is required!`;
* For `Private API` if parameter `token` is wrong, there are no authorized user in the system with provided token, you 
will get response with code `401` and message `Not Authorized!`;
* For methods that are not in API, you will get response with code `404` and message `Not Found`;
* For all uncovered exceptions you will get response with code `500`.

## Copyright

Copyright (C) 2014 Pavlo Voznenko.

Distributed under the MIT License.
