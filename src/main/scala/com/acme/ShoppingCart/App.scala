package com.acme.ShoppingCart

import com.acme.ShoppingCart.controllers.IndexApp
import com.acme.ShoppingCart.controllers.Api.{ProductsApi, CartProductsApi, UsersApi}
import com.twitter.finatra._

object App extends FinatraServer {
  register(new IndexApp)
  register(new UsersApi)
  register(new ProductsApi)
  register(new CartProductsApi)
}
