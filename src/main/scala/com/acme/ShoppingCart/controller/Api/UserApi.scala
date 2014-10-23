package com.acme.ShoppingCart.controller.Api

import com.twitter.finatra.Controller
import com.acme.ShoppingCart.helper.BearerTokenGenerator
import com.acme.ShoppingCart.model.UsersModel

class UserApi extends Controller {

  /**
   * Get authentication token
   *
   * curl http://localhost:7070/api/user/authentication
   */
  get("/api/user/authentication") { request =>
    val token = new BearerTokenGenerator().generateSHAToken("ShoppingCart")

    try {
      UsersModel add token
    } catch {
      case exception: Throwable => log.error(exception, "Adding user to DB failed!")
    }

    render.json(Map("token" -> token)).toFuture
  }
}
