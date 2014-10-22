package com.acme.ShoppingCart.controller.Api

import com.acme.ShoppingCart.model.{UsersModel, UserCartModel}
import com.twitter.finatra.Controller

class CartApi extends Controller {

  /**
   * Get all products for user
   */
  get("/api/cart") { request =>
    val userId = UsersModel.getByToken(request.params.getOrElse("token", null))
    render.json(UserCartModel.getUserProducts(userId)).toFuture
  }

  /**
   * Add new product to the user's shopping cart
   */
  put("/api/cart") { request =>
    try {
      val userId = UsersModel.getByToken(request.params.getOrElse("token", null))
      val productId = request.params.getInt("productId").head
      val product = UserCartModel.getUserProduct(userId, productId)

      if (product.isEmpty) UserCartModel.add(userId, productId)
      else UserCartModel.incProductQuantity(userId, productId)

      render.json(Map("response" -> "done")).toFuture
    } catch {
      case exception: Throwable =>
        log.error(exception, "Adding new product failed!")
        render.status(500).json(Seq("error" -> exception.toString)).toFuture
    }
  }

  /**
   * Update information regarding user product in the shopping cart
   */
  post("/api/cart") { request =>
    render.plain("ok").toFuture
  }

  /**
   * Remove item from user's cart
   */
  delete("/api/cart") { request =>
    render.plain("ok").toFuture
  }
}
