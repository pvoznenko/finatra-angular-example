package com.acme.ShoppingCart.controller.Api

import com.twitter.finatra.Controller

class UserApi extends Controller {
  import com.acme.ShoppingCart.helper.BearerTokenGenerator

  get("/api/user/authentication") { request =>
    render.json(Map("token" -> new BearerTokenGenerator().generateSHAToken("ShoppingCart"))).toFuture
  }
}
