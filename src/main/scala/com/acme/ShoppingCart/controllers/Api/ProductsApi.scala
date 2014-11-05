package com.acme.ShoppingCart.controllers.Api

import com.acme.ShoppingCart.controllers.ResponseController
import com.acme.ShoppingCart.models.ProductsModel

class ProductsApi extends ResponseController {

  /**
   * Get list of available products in our shop
   *
   * curl -i -X GET -G http://localhost:7070/api/products -d limit={limit.?}
   */
  get("/api/products")(checkRequestType(_) { request =>
    val limit = request.params.getInt("limit")
    val products = ProductsModel.getAll(limit)

    renderResponse(request, render, Some(200), Some(products))
  })
}
