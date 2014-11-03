package com.acme.ShoppingCart.traits

import com.acme.ShoppingCart.exception.Unauthorized
import com.acme.ShoppingCart.models.UsersModel
import com.twitter.finatra.Request

trait Users extends ParamsValidation {
  def getUserId(request: Request) = {
    val token = getParam(request.params, "token")

    UsersModel getUserByToken token match {
      case x +: xs => x.id.get
      case _ => throw new Unauthorized
    }
  }
}
