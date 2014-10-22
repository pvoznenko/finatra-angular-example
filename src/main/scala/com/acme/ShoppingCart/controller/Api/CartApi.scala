package com.acme.ShoppingCart.controller.Api

import com.twitter.finatra.Controller

class CartApi extends Controller {
  val products = Seq(
    Map("id" -> 1, "name" -> "Mac Book Retina 15'", "price" -> 1299, "quantity" -> 1),
    Map("id" -> 2, "name" -> "Nexus 6", "price" -> 649, "quantity" -> 2),
    Map("id" -> 3, "name" -> "Surface", "price" -> 750, "quantity" -> 1)
  )

  get("/api/cart") { request =>
    render.json(products).toFuture
  }
}
