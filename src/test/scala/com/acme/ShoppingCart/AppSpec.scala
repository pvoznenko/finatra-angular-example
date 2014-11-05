package com.acme.ShoppingCart

import com.acme.ShoppingCart.controllers.Api.{CartProductsApi, ProductsApi, UsersApi}
import com.twitter.finatra.test._
import com.twitter.finatra.FinatraServer
import com.acme.ShoppingCart.controllers.IndexApp
import scala.collection.Map
import scala.util.parsing.json.JSON

class AppSpec extends FlatSpecHelper {

  override val server = new FinatraServer

  server.register(new IndexApp())
  server.register(new UsersApi())
  server.register(new ProductsApi())
  server.register(new CartProductsApi())

  /**
   * Overall tests
   */

  "GET /not-found" should "respond 404" in {
    get("/not-found", Map(), Map("Accept" -> "application/json"))
    response.body   should equal ("Not Found")
    response.code   should equal (404)
  }

  "GET /api/products" should "respond 400 with message `No matching accepted Response format could be determined!`" in {
    get("/api/products", Map(), Map("Accept" -> "text/xml"))
    response.body.contains  ("No matching accepted Response format could be determined!") should equal(true)
    response.code   should equal (406)
  }

  "GET /api/products" should "respond 200" in {
    get("/api/products", Map(), Map("Accept" -> "application/json"))
    JSON.parseFull(response.body).get should equal(TestData.products)
    response.code   should equal (200)
  }

  "POST /api/users/authentication" should "respond 400 with message `No matching accepted Response format could be determined!`" in {
    post("/api/users/authentication", Map(), Map("Accept" -> "text/xml"))
    response.body.contains  ("No matching accepted Response format could be determined!") should equal(true)
    response.code   should equal (406)
  }

  "POST /api/users/authentication" should "respond 201" in {
    post("/api/users/authentication", Map(), Map("Accept" -> "application/json"))
    response.body.contains ("token") should equal(true)
    response.code   should equal (201)
  }

  "GET /api/users" should "respond 400 with message `No matching accepted Response format could be determined!`" in {
    get("/api/users", Map(), Map("Accept" -> "text/xml"))
    response.body.contains  ("No matching accepted Response format could be determined!") should equal(true)
    response.code   should equal (406)
  }

  "GET /api/users" should "respond 200" in {
    get("/api/users", Map(), Map("Accept" -> "application/json"))
    response.code   should equal (200)
  }

  "GET /index.html" should "respond 200" in {
    get("/")
    response.body.contains ("Your current user auth token") should equal(true)
    response.code should equal (200)
  }

  def getAuthToken = {
    post("/api/users/authentication", Map(), Map("Accept" -> "application/json"))
    response.code   should equal (201)

    type M = Map[String, String]

    JSON.parseFull(response.body) match {
      case Some(map: M) => map ++ Map("Accept" -> "application/json")
      case _ => Map("" -> "")
    }
  }

  /**
   * Get products for users cart tests
   */

  "GET /api/cart/products" should "respond 401 with message `Not Authorized!`" in {
    get("/api/cart/products", Map(), Map("Accept" -> "application/json"))
    response.body.contains ("Not Authorized!") should equal(true)
    response.code   should equal (401)

    get("/api/cart/products", Map(), Map("token" -> "") ++ Map("Accept" -> "application/json"))
    response.body.contains ("Not Authorized!") should equal(true)
    response.code   should equal (401)
  }

  "GET /api/cart/products" should "respond 400 with message `No matching accepted Response format could be determined!`" in {
    get("/api/cart/products", Map(), getAuthToken ++ Map("Accept" -> "text/xml"))
    response.body.contains  ("No matching accepted Response format could be determined!") should equal(true)
    response.code   should equal (406)
  }

  "GET /api/cart/products" should "respond 200" in {
    get("/api/cart/products", Map(), getAuthToken)
    print(response.body)
    response.code   should equal (200)
  }

  /**
   * Create products for users cart tests
   */

  "PUT /api/cart/products/1" should "respond 401 with message `Not Authorized!`" in {
    put("/api/cart/products/1", Map(), Map("Accept" -> "application/json"))
    response.body.contains ("Not Authorized!") should equal(true)
    response.code   should equal (401)

    put("/api/cart/products/1", Map(), Map("token" -> "") ++ Map("Accept" -> "application/json"))
    response.body.contains ("Not Authorized!") should equal(true)
    response.code   should equal (401)
  }

  "PUT /api/cart/products/abc" should "respond 400 with message `Illegal Argument!`" in {
    put("/api/cart/products/abc", Map(), getAuthToken)
    response.body.contains ("Illegal Argument!") should equal(true)
    response.code   should equal (400)
  }

  "PUT /api/cart/products/12" should "respond 404 with message `Product with provided id '12' is not exist!`" in {
    put("/api/cart/products/12", Map(), getAuthToken)
    response.body.contains ("Product with provided id '12' is not exist!") should equal(true)
    response.code   should equal (404)
  }

  def addProductWithId1 (token: Map[String, String]) = {
    put("/api/cart/products/1", Map(), token ++ Map("Accept" -> "application/json"))
    response.code   should equal (201)
  }

  "PUT /api/cart/products/1" should "respond 400 with message `No matching accepted Response format could be determined!`" in {
    put("/api/cart/products/1", Map(), getAuthToken ++ Map("Accept" -> "text/xml"))
    response.body.contains  ("No matching accepted Response format could be determined!") should equal(true)
    response.code   should equal (406)
  }

  "PUT /api/cart/products/1" should "respond 201" in {
    addProductWithId1(getAuthToken)
  }

  "PUT /api/cart/products/1 multiple times" should "respond 409 with message `Product is already in user's cart!`" in {
    val token = getAuthToken
    addProductWithId1(token)

    put("/api/cart/products/1", Map(), token ++ Map("Accept" -> "application/json"))
    response.body.contains ("Product is already in user's cart!") should equal(true)
    response.code   should equal (409)
  }

  /**
   * Update products for users cart tests
   */

  "PUT /api/cart/products/1/quantity/1" should "respond 401 with message `Not Authorized!`" in {
    put("/api/cart/products/1/quantity/1", Map(), Map("Accept" -> "application/json"))
    response.body.contains ("Not Authorized!") should equal(true)
    response.code   should equal (401)

    put("/api/cart/products/1/quantity/1", Map(), Map("token" -> "") ++ Map("Accept" -> "application/json"))
    response.body.contains ("Not Authorized!") should equal(true)
    response.code   should equal (401)
  }

  "PUT /api/cart/products/abc/quantity/1" should "respond 400 with message `Illegal Argument!`" in {
    put("/api/cart/products/abc/quantity/1", Map(), getAuthToken)
    response.body.contains ("Illegal Argument!") should equal(true)
    response.code   should equal (400)
  }

  "PUT /api/cart/products/12/quantity/1" should "respond 404 with message `Product with provided id '12' is not exist!`" in {
    put("/api/cart/products/12/quantity/1", Map(), getAuthToken)
    response.body.contains ("Product with provided id '12' is not exist!") should equal(true)
    response.code   should equal (404)
  }

  "PUT /api/cart/products/1/quantity/abc" should "respond 400 with message `Illegal Argument!`" in {
    put("/api/cart/products/1/quantity/abc", Map(), getAuthToken)
    response.body.contains ("Illegal Argument!") should equal(true)
    response.code   should equal (400)
  }

  "PUT /api/cart/products/1/quantity/-1" should "respond 400 with message `Quantity should be positive value!`" in {
    put("/api/cart/products/1/quantity/-1", Map(), getAuthToken)
    response.body.contains ("Quantity should be positive value!") should equal(true)
    response.code   should equal (400)
  }

  "PUT /api/cart/products/1/quantity/1" should "respond 404 with message `Product should be in user's cart!" in {
    put("/api/cart/products/1/quantity/1", Map(), getAuthToken)
    response.body.contains ("Product should be in user's cart!") should equal(true)
    response.code   should equal (404)
  }

  "PUT /api/cart/products/1/quantity/8" should "respond 400 with message `No matching accepted Response format could be determined!`" in {
    put("/api/cart/products/1/quantity/8", Map(), getAuthToken ++ Map("Accept" -> "text/xml"))
    response.body.contains  ("No matching accepted Response format could be determined!") should equal(true)
    response.code   should equal (406)
  }

  "PUT /api/cart/products/1/quantity/8" should "respond 204" in {
    val token = getAuthToken
    addProductWithId1(token)

    put("/api/cart/products/1/quantity/8", Map(), token ++ Map("Accept" -> "application/json"))
    response.code   should equal (204)
  }

  /**
   * Deletion products for users cart tests
   */

  "DELETE /api/cart/products/1" should "respond 401 with message `Not Authorized!`" in {
    delete("/api/cart/products/1", Map(), Map("Accept" -> "application/json"))
    response.body.contains ("Not Authorized!") should equal(true)
    response.code   should equal (401)

    delete("/api/cart/products/1", Map(), Map("token" -> "") ++ Map("Accept" -> "application/json"))
    response.body.contains ("Not Authorized!") should equal(true)
    response.code   should equal (401)
  }

  "DELETE /api/cart/products/abc" should "respond 400 with message `Illegal Argument!`" in {
    delete("/api/cart/products/abc", Map(), getAuthToken)
    response.body.contains ("Illegal Argument!") should equal(true)
    response.code   should equal (400)
  }

  "DELETE /api/cart/products/1" should "respond 404 with message `trying to remove product from user's shopping cart that is not there!`" in {
    delete("/api/cart/products/1", Map(), getAuthToken)
    response.body.contains ("trying to remove product from user's shopping cart that is not there!") should equal(true)
    response.code   should equal (404)
  }

  "DELETE /api/cart/products/12" should "respond 404 with message `Product with provided id '12' is not exist!`" in {
    delete("/api/cart/products/12", Map(), getAuthToken)
    response.body.contains ("Product with provided id '12' is not exist!") should equal(true)
    response.code   should equal (404)
  }

  "DELETE /api/cart/products/1" should "respond 400 with message `No matching accepted Response format could be determined!`" in {
    delete("/api/cart/products/1", Map(), getAuthToken ++ Map("Accept" -> "text/xml"))
    response.body.contains  ("No matching accepted Response format could be determined!") should equal(true)
    response.code   should equal (406)
  }

  "DELETE /api/cart/products/1" should "respond 204 and remove product" in {
    val token = getAuthToken

    put("/api/cart/products/1", Map(), token ++ Map("Accept" -> "application/json"))
    response.code   should equal (201)

    get("/api/cart/products", Map(), token ++ Map("Accept" -> "application/json"))
    JSON.parseFull(response.body).get should equal(TestData.firstProduct)

    delete("/api/cart/products/1", Map(), token ++ Map("Accept" -> "application/json"))
    response.code   should equal (204)

    get("/api/cart/products", Map(), token ++ Map("Accept" -> "application/json"))
    JSON.parseFull(response.body).get should equal(List())
  }
}
