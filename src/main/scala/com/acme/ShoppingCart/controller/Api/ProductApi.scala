package com.acme.ShoppingCart.controller.Api

import com.acme.ShoppingCart.model.ProductsModel
import com.twitter.finatra.Controller

class ProductApi extends Controller {

  /**
   * Get list of available products in our shop
   */
  get("/api/product") { request =>
    render.json(ProductsModel.getAll).toFuture
  }
}
