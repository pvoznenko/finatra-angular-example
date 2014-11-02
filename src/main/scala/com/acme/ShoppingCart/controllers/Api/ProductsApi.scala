package com.acme.ShoppingCart.controllers.Api

import com.acme.ShoppingCart.models.ProductsModel
import com.twitter.finatra.Controller

class ProductsApi extends Controller {

  /**
   * Get list of available products in our shop
   *
   * curl http://localhost:7070/api/product
   */
  get("/api/products") { request =>
    render.json(ProductsModel.getAll).toFuture
  }
}
