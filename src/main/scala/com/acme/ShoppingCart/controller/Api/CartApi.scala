package com.acme.ShoppingCart.controller.Api

import com.acme.ShoppingCart.model.{UsersModel, UserCartModel}
import com.twitter.finatra.Controller

class CartApi extends Controller {

  /**
   * Get all products for user
   *
   * curl -i -X GET -d "token={auth_token}" http://localhost:7070/api/cart
   */
  get("/api/cart") { request =>
    val userId = UsersModel.getByToken(request.params.getOrElse("token", null))
    render.json(UserCartModel.getUserProducts(userId)).toFuture
  }

  /**
   * Add new product to the user's shopping cart
   *
   * curl -i -X PUT -d "token={auth_token}&productId={product_id}" http://localhost:7070/api/cart
   */
  put("/api/cart") { request =>
    try {
      val userId = UsersModel.getByToken(request.params.get("token").get)
      val productId = request.params.getInt("productId").get
      val product = UserCartModel.getUserProduct(userId, productId)

      if (product.isEmpty) UserCartModel.add(userId, productId)
      else UserCartModel.updateProductQuantity(userId, productId)

      render.json(Map("response" -> "done")).toFuture
    } catch {
      case exception: Throwable =>
        log.error(exception, "Adding new product failed!")
        render.status(500).json(Seq("error" -> exception.toString)).toFuture
    }
  }

  /**
   * Update information regarding user product in the shopping cart
   *
   * curl -i -X POST -d "token={auth_token}&productId={product_id}&quantity={quantity}" http://localhost:7070/api/cart
   */
  post("/api/cart") { request =>
    try {
      val userId = UsersModel.getByToken(request.params.get("token").get)
      val productId = request.params.getInt("productId").get
      val quantity = request.params.getInt("quantity")
      val product = UserCartModel.getUserProduct(userId, productId)

      if (product.isEmpty) throw new Exception("You can not update unexciting product!")
      else UserCartModel.updateProductQuantity(userId, productId, quantity)

      render.json(Map("response" -> "done")).toFuture
    } catch {
      case exception: Throwable =>
        log.error(exception, "Updating product failed!")
        render.status(500).json(Seq("error" -> exception.toString)).toFuture
    }
  }

  /**
   * Remove item from user's cart
   *
   * curl -i -X DELETE -d "token={auth_token}&productId={product_id}" http://localhost:7070/api/cart
   */
  delete("/api/cart") { request =>
    try {
      val userId = UsersModel.getByToken(request.params.get("token").get)
      val productId = request.params.getInt("productId").get

      UserCartModel.remove(userId, productId)

      render.json(Map("response" -> "done")).toFuture
    } catch {
      case exception: Throwable =>
        log.error(exception, "Removing product failed!")
        render.status(500).json(Seq("error" -> exception.toString)).toFuture
    }
  }
}
