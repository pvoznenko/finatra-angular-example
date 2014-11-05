package com.acme.ShoppingCart.traits

import com.acme.ShoppingCart.exception.{BadRequestException, UnauthorizedException}
import com.acme.ShoppingCart.dao.UsersDAO
import com.twitter.finatra.Request

trait UsersTrait extends ParamsValidationTrait {
  def getUserId(request: Request) =
    try {
      val token = getParam(request.headerMap, "token")

      UsersDAO getUserByToken token match {
        case x +: xs => x.id.get
        case _ => throw new UnauthorizedException
      }
    } catch {
      case exception: BadRequestException => throw new UnauthorizedException
    }
}
