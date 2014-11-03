package com.acme.ShoppingCart.traits

import com.acme.ShoppingCart.exception.BadRequest
import scala.collection.Map

trait ParamsValidation {
  def getParam(params: Map[String, String], paramKey: String) =
    params.get(paramKey) match {
      case Some(param: String) => param
      case _ => throw new BadRequest("Parameter '" ++ paramKey ++ "' is required!")
    }
}
