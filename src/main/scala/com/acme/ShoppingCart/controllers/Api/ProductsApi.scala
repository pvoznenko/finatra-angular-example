package com.acme.ShoppingCart.controllers.Api

import com.acme.ShoppingCart.models.ProductsModel
import com.twitter.finatra.Controller

class ProductsApi extends Controller {

  /**
   * Get list of available products in our shop
   *
   * curl -i -X GET -G http://localhost:7070/api/products -d limit={limit.?}
   */
  get("/api/products") { request =>
    val limit = request.params.getInt("limit")
    val products = ProductsModel.getAll(limit)

    render.json(products).toFuture
  }
}
