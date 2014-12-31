package com.acme.ShoppingCart.traits

import com.acme.ShoppingCart.exceptions.{BadRequestException, UnauthorizedException}
import com.acme.ShoppingCart.models.UsersModel
import com.twitter.finatra.Request

trait UsersTrait extends ParamsValidationTrait {
  def getUserId(request: Request) =
    try {
      val token = getParam(request.headerMap, "token")

      UsersModel getUserByToken token match {
        case x +: xs => x.id.get
        case _ => throw new UnauthorizedException
      }
    } catch {
      case exception: BadRequestException => throw new UnauthorizedException
    }
}
