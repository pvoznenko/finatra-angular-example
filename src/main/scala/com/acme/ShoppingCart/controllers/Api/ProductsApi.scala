package com.acme.ShoppingCart.controllers.Api

import com.acme.ShoppingCart.controllers.ResponseController
import com.acme.ShoppingCart.API
import com.acme.ShoppingCart.models.ProductsModel

class ProductsApi extends ResponseController {

  /**
   * Get list of available products in our shop
   *
   * curl -i -H Accept:application/json -X GET -G http://localhost:7070/api/v3/products -d limit={limit.?}
   */
  get(API.getBaseUrl ++ "/products")(checkRequestType(_) { request =>
    val limit = request.params.getInt("limit")
    val products = ProductsModel.getAll(limit)

    renderResponse(request, render, Some(200), Some(products))
  })
}
