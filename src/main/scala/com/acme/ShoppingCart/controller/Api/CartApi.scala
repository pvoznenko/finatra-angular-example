package com.acme.ShoppingCart.controller.Api

import com.acme.ShoppingCart.model.{UsersModel, UserCartModel}
import com.twitter.finatra.Controller

class CartApi extends Controller {

  /**
   * Get all products for user
   *
   * curl -X GET -G http://localhost:7070/api/cart -d token={token}
   */
  get("/api/cart") { request =>
    val userId = UsersModel.getByToken(request.params.getOrElse("token", null))
    render.json(UserCartModel.getUserProducts(userId)).toFuture
  }

  /**
   * Add new product to the user's shopping cart
   *
   * curl -X PUT http://localhost:7070/api/cart -d productId={product_id} -d token={token}
   */
  put("/api/cart") { request =>
    val userId = UsersModel.getByToken(request.params.getOrElse("token", null))
    val productId = request.params.getInt("productId").get
    val product = UserCartModel.getUserProduct(userId, productId)

    if (product.isEmpty) UserCartModel.add(userId, productId)
    else UserCartModel.updateProductQuantity(userId, productId)

    render.json(Map("response" -> "done")).toFuture
  }

  /**
   * Update information regarding user product in the shopping cart
   *
   * curl -X POST http://localhost:7070/api/cart -d productId={product_id} -d token={token} -d quantity={quantity.?}
   */
  post("/api/cart") { request =>
    val userId = UsersModel.getByToken(request.params.getOrElse("token", null))
    val productId = request.params.getInt("productId").get
    val quantity = request.params.getInt("quantity")
    val product = UserCartModel.getUserProduct(userId, productId)

    if (product.isEmpty) throw new Exception("You can not update unexisting product!")
    else UserCartModel.updateProductQuantity(userId, productId, quantity)

    render.json(Map("response" -> "done")).toFuture
  }

  /**
   * Remove item from user's cart
   *
   * curl -X DELETE -G http://localhost:7070/api/cart -d token={token} -d productId={product_id}
   */
  delete("/api/cart") { request =>
    val userId = UsersModel.getByToken(request.params.getOrElse("token", null))
    val productId = request.params.getInt("productId").get

    UserCartModel.remove(userId, productId)

    render.json(Map("response" -> "done")).toFuture
  }
}
