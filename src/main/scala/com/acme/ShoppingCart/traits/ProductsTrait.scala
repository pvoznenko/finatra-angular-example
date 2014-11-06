package com.acme.ShoppingCart.traits

import com.acme.ShoppingCart.exceptions.{NotFoundException, BadRequestException}
import com.acme.ShoppingCart.dao.ProductsDAO
import com.twitter.finatra.Request

trait ProductsTrait extends ParamsValidationTrait {
  def getProductId(request: Request) = {
    val productId = getParam(request.routeParams, "productId")

    ProductsDAO getProductById productId.toInt match {
      case x +: xs => x.id.get
      case _ => throw new NotFoundException("Product with provided id '" ++ productId ++ "' is not exist!")
    }
  }

  def getProductQuantity(request: Request) = {
    val quantity = getParam(request.routeParams, "quantity").toInt

    quantity match {
      case amount if amount < 0 => throw new BadRequestException("Quantity should be positive value!")
      case _ => quantity
    }
  }
}
