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

  "GET /notfound" should "respond 404" in {
    get("/notfound")
    response.body   should equal ("not found")
    response.code   should equal (404)
  }

  "GET /api/cart/products" should "respond 401" in {
    get("/api/cart/products")
    response.body   should equal ("Not Authorized!")
    response.code   should equal (401)
  }

  "PUT /api/cart/products/1" should "respond 401" in {
    put("/api/cart/products/1")
    response.body   should equal ("Not Authorized!")
    response.code   should equal (401)
  }

  "POST /api/cart/products/1" should "respond 401" in {
    post("/api/cart/products/1")
    response.body   should equal ("Not Authorized!")
    response.code   should equal (401)
  }

  "DELETE /api/cart/products/1" should "respond 401" in {
    delete("/api/cart/products/1")
    response.body   should equal ("Not Authorized!")
    response.code   should equal (401)
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

  "Authorized user" should "respond 200" in {
    get("/api/cart/products", getAuthToken)
    response.code   should equal (200)
  }

  "Authorized user" should "add product once" in {
    val token = getAuthToken
    put("/api/cart/products/1", token)
    response.code   should equal (200)

    get("/api/cart/products", token)
    response.code   should equal (200)
    JSON.parseFull(response.body).get should equal(TestData.firstProduct)
  }

  "Authorized user by adding product more then once" should "increase quantity" in {
    val token = getAuthToken
    put("/api/cart/products/1", token)
    response.code   should equal (200)

    put("/api/cart/products/1", token)
    response.code   should equal (200)

    get("/api/cart/products", token)
    response.code   should equal (200)
    JSON.parseFull(response.body).get should equal(TestData.updatedProduct1)
  }

  "Authorized user add product without product id" should "get error 404" in {
    put("/api/cart/products", getAuthToken)
    response.code   should equal (404)
  }

  "Authorized user" should "update product quantity" in {
    val token = getAuthToken

    put("/api/cart/products/1", token)
    response.code   should equal (200)

    post("/api/cart/products/1", token)
    response.code   should equal (200)

    get("/api/cart/products", token)
    JSON.parseFull(response.body).get should equal(TestData.updatedProduct1)
  }

  "Authorized user" should "specify product quantity" in {
    val token = getAuthToken

    put("/api/cart/products/1", token)
    response.code   should equal (200)

    post("/api/cart/products/1", token ++ Map("quantity" -> "10"))
    response.code   should equal (200)

    get("/api/cart/products", token)
    JSON.parseFull(response.body).get should equal(TestData.updatedProduct2)
  }

  "Authorized user update product without product id" should "get error 404" in {
    val token = getAuthToken

    put("/api/cart/products/1", token)
    response.code   should equal (200)

    post("/api/cart/products", token)
    response.code   should equal (404)
  }

  "Authorized user update product that he do not have" should "get error 500" in {
    post("/api/cart/products/1", getAuthToken)
    response.code   should equal (500)
  }

  "Authorized user" should "remove product" in {
    val token = getAuthToken

    put("/api/cart/products/1", token)
    response.code   should equal (200)

    get("/api/cart/products", token)
    JSON.parseFull(response.body).get should equal(TestData.firstProduct)

    delete("/api/cart/products/1", token)
    response.code   should equal (200)

    get("/api/cart/products", token)
    JSON.parseFull(response.body).get should equal(List())
  }

  "Authorized user remove product without product id" should "get error 404" in {
    delete("/api/cart/products", getAuthToken)
    response.code   should equal (404)
  }
}
