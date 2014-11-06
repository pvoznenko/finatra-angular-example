package com.acme.ShoppingCart.traits

import com.acme.ShoppingCart.exceptions.BadRequestException
import scala.collection.Map

trait ParamsValidationTrait {
  def getParam(params: Map[String, String], paramKey: String) =
    params.get(paramKey) match {
      case Some(param: String) => param
      case _ => throw new BadRequestException("Parameter '" ++ paramKey ++ "' is required!")
    }
}
