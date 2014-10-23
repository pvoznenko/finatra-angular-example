package com.acme.ShoppingCart

import com.acme.ShoppingCart.controller.IndexApp
import com.acme.ShoppingCart.controller.Api.{ProductApi, CartApi, UserApi}
import com.twitter.finatra._

object App extends FinatraServer {
  register(new IndexApp())
  register(new UserApi())
  register(new ProductApi())
  register(new CartApi())
}
