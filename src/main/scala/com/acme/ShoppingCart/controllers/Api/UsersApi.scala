package com.acme.ShoppingCart.controllers.Api

import com.twitter.finatra.Controller
import com.acme.ShoppingCart.helpers.BearerTokenGenerator
import com.acme.ShoppingCart.models.UsersModel

class UsersApi extends Controller {

  /**
   * Get authentication token
   *
   * curl -i -X POST http://localhost:7070/api/users/authentication
   */
  post("/api/users/authentication") { request =>
    val token = new BearerTokenGenerator generateSHAToken "ShoppingCart"

    UsersModel add token

    val response = Map("token" -> token)

    render.status(201).json(response).toFuture
  }

  /**
   * Method only for debugging - will return list of users
   *
   * curl -i -X GET -G http://localhost:7070/api/users -d limit={limit.?}
   */
  get("/api/users") { request =>
    val limit = request.params.getInt("limit")
    val users = UsersModel.getAll(limit)

    render.json(users).toFuture
  }
}
