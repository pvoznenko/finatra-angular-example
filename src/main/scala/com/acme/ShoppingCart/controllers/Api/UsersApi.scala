package com.acme.ShoppingCart.controllers.Api

import com.acme.ShoppingCart.controllers.ResponseController
import com.acme.ShoppingCart.helpers.BearerTokenGeneratorHelper
import com.acme.ShoppingCart.API
import com.acme.ShoppingCart.models.UsersModel
import com.twitter.util.Future
import scala.util.{Success, Failure, Try}

class UsersApi extends ResponseController {

  /**
   * Get authentication token
   *
   * curl -i -H Accept:application/json -X POST http://localhost:7070/api/v3/users/authentication
   */
  post(API.getBaseUrl ++ "/users/authentication")(checkRequestType(_) { request =>
    (for {
      token <- Try(new BearerTokenGeneratorHelper generateSHAToken "ShoppingCart")
      addedRows <- Try(UsersModel add token)
    } yield {
      Map("token" -> token)
    }) match {
      case Failure(error) => Future exception error
      case Success(response) => renderResponse(request, render, Some(201), Some(response))
    }
  })

  /**
   * Method only for debugging - will return list of users
   *
   * curl -i -X GET -G http://localhost:7070/api/v3/users -d limit={limit.?}
   */
  get(API.getBaseUrl ++ "/users")(checkRequestType(_) { request =>
    (for {
      limit <- Try(request.params getInt "limit")
      users <- Try(UsersModel getAll limit)
    } yield {
      users
    }) match {
      case Failure(error) => Future exception error
      case Success(users) => renderResponse(request, render, Some(200), Some(users))
    }
  })
}
