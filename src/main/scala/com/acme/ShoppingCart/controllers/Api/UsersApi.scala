package com.acme.ShoppingCart.controllers.Api

import com.twitter.finatra.Controller
import com.acme.ShoppingCart.helpers.BearerTokenGenerator
import com.acme.ShoppingCart.models.UsersModel

class UsersApi extends Controller {

  /**
   * Get authentication token
   *
   * curl http://localhost:7070/api/user/authentication
   */
  get("/api/users/authentication") { request =>
    val token = new BearerTokenGenerator().generateSHAToken("ShoppingCart")

    try {
      UsersModel add token
    } catch {
      case exception: Throwable => log.error(exception, "Adding user to DB failed!")
    }

    render.json(Map("token" -> token)).toFuture
  }

  /**
   * Method only for debugging - will return list of users
   *
   * curl http://localhost:7070/api/user
   */
  get("/api/users") { request =>
    render.json(UsersModel.getAll).toFuture
  }
}
