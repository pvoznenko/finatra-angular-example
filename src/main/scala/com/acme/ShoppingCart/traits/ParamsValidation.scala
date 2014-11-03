package com.acme.ShoppingCart.traits

import com.acme.ShoppingCart.exception.{Unauthorized, BadRequest}
import com.acme.ShoppingCart.models.{ProductsModel, UsersModel}
import com.twitter.finatra.Request
import scala.collection.Map

trait ParamsValidation {
  private [this] def getParam(params: Map[String, String], paramKey: String) = {
    params.get(paramKey) match {
      case Some(param: String) => param
      case _ => throw new BadRequest("Parameter '" ++ paramKey ++ "' is required!")
    }
  }

  def getUserId(request: Request) = {
    val token = getParam(request.params, "token")

    UsersModel getUserByToken token match {
      case x +: xs => x.id.get
      case _ => throw new Unauthorized
    }
  }

  def getProductId(request: Request) = {
    val productId = getParam(request.routeParams, "productId")

    ProductsModel getProductById productId.toInt match {
      case x +: xs => x.id.get
      case _ => throw new BadRequest("Product with provided id '" ++ productId ++ "' is not exist!")
    }
  }
}
