package com.acme.ShoppingCart

import com.acme.ShoppingCart.controllers.Api.{CartApi, ProductsApi, UsersApi}
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
  server.register(new CartApi())

  "GET /notfound" should "respond 404" in {
    get("/notfound")
    response.body   should equal ("not found")
    response.code   should equal (404)
  }

  "GET /api/cart" should "respond 401" in {
    get("/api/cart")
    response.body   should equal ("Not Authorized!")
    response.code   should equal (401)
  }

  "PUT /api/cart" should "respond 401" in {
    put("/api/cart")
    response.body   should equal ("Not Authorized!")
    response.code   should equal (401)
  }

  "POST /api/cart" should "respond 401" in {
    post("/api/cart")
    response.body   should equal ("Not Authorized!")
    response.code   should equal (401)
  }

  "DELETE /api/cart" should "respond 401" in {
    delete("/api/cart")
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
    get("/api/cart", getAuthToken)
    response.code   should equal (200)
  }

  "Authorized user" should "add product once" in {
    val token = getAuthToken
    put("/api/cart", token ++ Map("productId" -> "1"))
    response.code   should equal (200)

    get("/api/cart", token)
    response.code   should equal (200)
    JSON.parseFull(response.body).get should equal(TestData.firstProduct)
  }

  "Authorized user by adding product more then once" should "increase quantity" in {
    val token = getAuthToken
    put("/api/cart", token ++ Map("productId" -> "1"))
    response.code   should equal (200)

    put("/api/cart", token ++ Map("productId" -> "1"))
    response.code   should equal (200)

    get("/api/cart", token)
    response.code   should equal (200)
    JSON.parseFull(response.body).get should equal(TestData.updatedProduct1)
  }

  "Authorized user add product without product id" should "get error 500" in {
    put("/api/cart", getAuthToken)
    response.code   should equal (500)
  }

  "Authorized user" should "update product quantity" in {
    val token = getAuthToken

    put("/api/cart", token ++ Map("productId" -> "1"))
    response.code   should equal (200)

    post("/api/cart", token ++ Map("productId" -> "1"))
    response.code   should equal (200)

    get("/api/cart", token)
    JSON.parseFull(response.body).get should equal(TestData.updatedProduct1)
  }

  "Authorized user" should "specify product quantity" in {
    val token = getAuthToken

    put("/api/cart", token ++ Map("productId" -> "1"))
    response.code   should equal (200)

    post("/api/cart", token ++ Map("productId" -> "1", "quantity" -> "10"))
    response.code   should equal (200)

    get("/api/cart", token)
    JSON.parseFull(response.body).get should equal(TestData.updatedProduct2)
  }

  "Authorized user update product without product id" should "get error 500" in {
    val token = getAuthToken

    put("/api/cart", token ++ Map("productId" -> "1"))
    response.code   should equal (200)

    post("/api/cart", token)
    response.code   should equal (500)
  }

  "Authorized user update product that he do not have" should "get error 500" in {
    post("/api/cart", getAuthToken ++ Map("productId" -> "1"))
    response.code   should equal (500)
  }

  "Authorized user" should "remove product" in {
    val token = getAuthToken

    put("/api/cart", token ++ Map("productId" -> "1"))
    response.code   should equal (200)

    get("/api/cart", token)
    JSON.parseFull(response.body).get should equal(TestData.firstProduct)

    delete("/api/cart", token ++ Map("productId" -> "1"))
    response.code   should equal (200)

    get("/api/cart", token)
    JSON.parseFull(response.body).get should equal(List())
  }

  "Authorized user remove product without product id" should "get error 500" in {
    delete("/api/cart", getAuthToken)
    response.code   should equal (500)
  }
}
