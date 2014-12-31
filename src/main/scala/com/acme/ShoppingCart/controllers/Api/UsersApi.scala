package com.acme.ShoppingCart.controllers.Api

import com.acme.ShoppingCart.controllers.ResponseController
import com.acme.ShoppingCart.helpers.BearerTokenGeneratorHelper
import com.acme.ShoppingCart.API
import com.acme.ShoppingCart.models.UsersModel

class UsersApi extends ResponseController {

  /**
   * Get authentication token
   *
   * curl -i -H Accept:application/json -X POST http://localhost:7070/api/v3/users/authentication
   */
  post(API.getBaseUrl ++ "/users/authentication")(checkRequestType(_) { request =>
    val token = new BearerTokenGeneratorHelper generateSHAToken "ShoppingCart"
    val response = Map("token" -> token)

    UsersModel add token

    renderResponse(request, render, Some(201), Some(response))
  })

  /**
   * Method only for debugging - will return list of users
   *
   * curl -i -X GET -G http://localhost:7070/api/v3/users -d limit={limit.?}
   */
  get(API.getBaseUrl ++ "/users")(checkRequestType(_) { request =>
    val limit = request.params.getInt("limit")
    val users = UsersModel.getAll(limit)

    renderResponse(request, render, Some(200), Some(users))
  })
}
