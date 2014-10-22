package com.acme.ShoppingCart.controller.Api

import com.twitter.finatra.Controller

class ProductApi extends Controller {
  val products = Seq(
    Map("id" -> 1, "name" -> "Mac Book Retina 15'", "price" -> 1299),
    Map("id" -> 2, "name" -> "Nexus 6", "price" -> 649),
    Map("id" -> 3, "name" -> "Surface", "price" -> 750)
  )

  get("/api/product") { request =>
    render.json(products).toFuture
  }
}
