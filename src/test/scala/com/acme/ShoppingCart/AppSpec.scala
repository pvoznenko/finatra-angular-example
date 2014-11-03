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
    get("/not-found")
    response.body   should equal ("Not Found")
    response.code   should equal (404)
  }

  "GET /api/products" should "respond 200" in {
    get("/api/products")
    JSON.parseFull(response.body).get should equal(TestData.products)
    response.code   should equal (200)
  }

  "POST /api/users/authentication" should "respond 201" in {
    post("/api/users/authentication")
    response.body.contains ("token") should equal(true)
    response.code   should equal (201)
  }

  "GET /api/users" should "respond 200" in {
    get("/api/users")
    response.code   should equal (200)
  }

  "GET /index.html" should "respond 200" in {
    get("/")
    response.body.contains ("Your current user auth token") should equal(true)
    response.code should equal (200)
  }

  def getAuthToken = {
    post("/api/users/authentication")
    response.code   should equal (201)
    JSON.parseFull(response.body) match {
      case Some(map: Map[String, String]) => map
      case _ => Map("" -> "")
    }
  }

  /**
   * Get products for users cart tests
   */

  "GET /api/cart/products" should "respond 400 with message `Parameter 'token' is required!`" in {
    get("/api/cart/products")
    response.body   should equal ("Parameter 'token' is required!")
    response.code   should equal (400)
  }

  "GET /api/cart/products" should "respond 401 with message `Not Authorized!`" in {
    get("/api/cart/products", Map("token" -> ""))
    response.body   should equal ("Not Authorized!")
    response.code   should equal (401)
  }

  "GET /api/cart/products" should "respond 200" in {
    get("/api/cart/products", getAuthToken)
    response.code   should equal (200)
  }

  /**
   * Create products for users cart tests
   */

  "PUT /api/cart/products/1" should "respond 400 with message `Parameter 'token' is required!`" in {
    put("/api/cart/products/1")
    response.body   should equal ("Parameter 'token' is required!")
    response.code   should equal (400)
  }

  "PUT /api/cart/products/1" should "respond 401 with message `Not Authorized!`" in {
    put("/api/cart/products/1", Map("token" -> ""))
    response.body   should equal ("Not Authorized!")
    response.code   should equal (401)
  }

  "PUT /api/cart/products/abc" should "respond 400 with message `Illegal Argument!`" in {
    put("/api/cart/products/abc", getAuthToken)
    response.body   should equal ("Illegal Argument!")
    response.code   should equal (400)
  }

  "PUT /api/cart/products/12" should "respond 400 with message `Product with provided id '12' is not exist!`" in {
    put("/api/cart/products/12", getAuthToken)
    response.body   should equal ("Product with provided id '12' is not exist!")
    response.code   should equal (400)
  }

  def addProductWithId1 (token: Map[String, String]) = {
    put("/api/cart/products/1", token)
    response.code   should equal (201)
    JSON.parseFull(response.body).get should equal(TestData.addedProduct)
  }

  "PUT /api/cart/products/1" should "respond 201" in {
    addProductWithId1(getAuthToken)
  }

  "PUT /api/cart/products/1 multiple times" should "respond 409 with message `Products is already in user's cart!`" in {
    val token = getAuthToken
    addProductWithId1(token)

    put("/api/cart/products/1", token)
    response.body   should equal ("Products is already in user's cart!")
    response.code   should equal (409)
  }

  /**
   * Update products for users cart tests
   */

  "POST /api/cart/products/1/quantity/1" should "respond 400 with message `Parameter 'token' is required!`" in {
    post("/api/cart/products/1/quantity/1")
    response.body   should equal ("Parameter 'token' is required!")
    response.code   should equal (400)
  }

  "POST /api/cart/products/1/quantity/1" should "respond 401 with message `Not Authorized!`" in {
    post("/api/cart/products/1/quantity/1", Map("token" -> ""))
    response.body   should equal ("Not Authorized!")
    response.code   should equal (401)
  }

  "POST /api/cart/products/abc/quantity/1" should "respond 400 with message `Illegal Argument!`" in {
    post("/api/cart/products/abc/quantity/1", getAuthToken)
    response.body   should equal ("Illegal Argument!")
    response.code   should equal (400)
  }

  "POST /api/cart/products/12/quantity/1" should "respond 400 with message `Product with provided id '12' is not exist!`" in {
    post("/api/cart/products/12/quantity/1", getAuthToken)
    response.body   should equal ("Product with provided id '12' is not exist!")
    response.code   should equal (400)
  }

  "POST /api/cart/products/1/quantity/abc" should "respond 400 with message `Illegal Argument!`" in {
    post("/api/cart/products/1/quantity/abc", getAuthToken)
    response.body   should equal ("Illegal Argument!")
    response.code   should equal (400)
  }

  "POST /api/cart/products/1/quantity/-1" should "respond 400 with message `Quantity should be positive value!`" in {
    post("/api/cart/products/1/quantity/-1", getAuthToken)
    response.body   should equal ("Quantity should be positive value!")
    response.code   should equal (400)
  }

  "POST /api/cart/products/1/quantity/1" should "respond 400 with message `Product should be in user's cart!" in {
    post("/api/cart/products/1/quantity/1", getAuthToken)
    response.body   should equal ("Product should be in user's cart!")
    response.code   should equal (400)
  }

  "POST /api/cart/products/1/quantity/8" should "respond 204" in {
    val token = getAuthToken
    addProductWithId1(token)

    post("/api/cart/products/1/quantity/8", token)
    response.code   should equal (204)
  }

  /**
   * Deletion products for users cart tests
   */

  "DELETE /api/cart/products/1" should "respond 400 with message `Parameter 'token' is required!`" in {
    delete("/api/cart/products/1")
    response.body   should equal ("Parameter 'token' is required!")
    response.code   should equal (400)
  }

  "DELETE /api/cart/products/1" should "respond 401 with message `Not Authorized!`" in {
    delete("/api/cart/products/1", Map("token" -> ""))
    response.body   should equal ("Not Authorized!")
    response.code   should equal (401)
  }

  "DELETE /api/cart/products/abc" should "respond 400 with message `Illegal Argument!`" in {
    delete("/api/cart/products/abc", getAuthToken)
    response.body   should equal ("Illegal Argument!")
    response.code   should equal (400)
  }

  "DELETE /api/cart/products/1" should "respond 400 with message `trying to remove product from user's shopping cart that is not there!`" in {
    delete("/api/cart/products/1", getAuthToken)
    response.body   should equal ("trying to remove product from user's shopping cart that is not there!")
    response.code   should equal (400)
  }

  "DELETE /api/cart/products/12" should "respond 400 with message `Product with provided id '12' is not exist!`" in {
    delete("/api/cart/products/12", getAuthToken)
    response.body   should equal ("Product with provided id '12' is not exist!")
    response.code   should equal (400)
  }

  "DELETE /api/cart/products/1" should "respond 204 and remove product" in {
    val token = getAuthToken

    put("/api/cart/products/1", token)
    response.code   should equal (201)

    get("/api/cart/products", token)
    JSON.parseFull(response.body).get should equal(TestData.firstProduct)

    delete("/api/cart/products/1", token)
    response.code   should equal (204)

    get("/api/cart/products", token)
    JSON.parseFull(response.body).get should equal(List())
  }
}
