package com.acme.ShoppingCart.controllers.Api

import com.acme.ShoppingCart.controllers.ResponseController
import com.acme.ShoppingCart.API
import com.acme.ShoppingCart.models.ProductsModel
import com.twitter.util.Future
import scala.util.{Success, Failure, Try}

class ProductsApi extends ResponseController {

  /**
   * Get list of available products in our shop
   *
   * curl -i -H Accept:application/json -X GET -G http://localhost:7070/api/v3/products -d limit={limit.?}
   */
  get(API.getBaseUrl ++ "/products")(checkRequestType(_) { request =>
    (for {
      limit <- Try(request.params getInt "limit")
      products <- Try(ProductsModel getAll limit)
    } yield {
      products
    }) match {
      case Failure(error) => Future exception error
      case Success(products) => renderResponse(request, render, Some(200), Some(products))
    }
  })
}
