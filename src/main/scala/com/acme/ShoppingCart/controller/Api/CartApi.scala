package com.acme.ShoppingCart.controller.Api

import com.twitter.finatra.Controller

class CartApi extends Controller {
  val products = Seq(
    Map("id" -> 1, "name" -> "Mac Book Retina 15'", "price" -> 1299, "quantity" -> 1),
    Map("id" -> 2, "name" -> "Nexus 6", "price" -> 649, "quantity" -> 2),
    Map("id" -> 3, "name" -> "Surface", "price" -> 750, "quantity" -> 1)
  )

  /**
   * Get all products for user
   */
  get("/api/cart") { request =>
    render.json(products).toFuture
  }

  /**
   * Add new product to the user's shopping cart
   */
  put("/api/cart") { request =>
    val token = request.params.getOrElse("token", null)

    render.json().toFuture
  }

  /**
   * Update information regarding user product in the shopping cart
   */
  post("/api/cart") { request =>
    render.plain("ok").toFuture
  }

  /**
   * Remove item from user's cart
   */
  delete("/api/cart") { request =>
    render.plain("ok").toFuture
  }
}
