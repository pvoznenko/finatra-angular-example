package com.acme.ShoppingCart.controllers.Api

import com.acme.ShoppingCart.controllers.ResponseController
import com.acme.ShoppingCart.helpers.BearerTokenGeneratorHelper
import com.acme.ShoppingCart.models.UsersModel

class UsersApi extends ResponseController {

  /**
   * Get authentication token
   *
   * curl -i -X POST http://localhost:7070/api/users/authentication
   */
  post("/api/users/authentication")(checkRequestType(_) { request =>
    val token = new BearerTokenGeneratorHelper generateSHAToken "ShoppingCart"
    val response = Map("token" -> token)

    UsersModel add token

    render.status(201).json(response).toFuture
  })

  /**
   * Method only for debugging - will return list of users
   *
   * curl -i -X GET -G http://localhost:7070/api/users -d limit={limit.?}
   */
  get("/api/users")(checkRequestType(_) { request =>
    val limit = request.params.getInt("limit")
    val users = UsersModel.getAll(limit)

    render.json(users).toFuture
  })
}
