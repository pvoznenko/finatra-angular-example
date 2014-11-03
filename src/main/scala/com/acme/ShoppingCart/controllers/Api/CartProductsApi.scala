package com.acme.ShoppingCart.controllers.Api

import com.acme.ShoppingCart.exception.BadRequest
import com.acme.ShoppingCart.models.{UsersModel, UserCartModel}
import com.acme.ShoppingCart.traits.ParamsValidation
import com.twitter.finatra.Controller

class CartProductsApi extends Controller with ParamsValidation {

  /**
   * Get all products for user
   *
   * curl -X GET -G http://localhost:7070/api/cart/products -d token={token}
   */
  get("/api/cart/products") { request =>
    val userId = UsersModel.getByToken(request.params.getOrElse("token", null))
    render.json(UserCartModel.getUserProducts(userId)).toFuture
  }

  /**
   * Add new product to the user's shopping cart
   *
   * curl -X PUT http://localhost:7070/api/cart/products/{product_id} -d token={token}
   */
  put("/api/cart/products/:productId") { request =>
    val userId = UsersModel.getByToken(request.params.getOrElse("token", null))
    val productId = request.routeParams.get("productId").get.toInt
    val product = UserCartModel.getUserProduct(userId, productId)

    if (product.isEmpty) UserCartModel.add(userId, productId)
    else UserCartModel.updateProductQuantity(userId, productId)

    render.json(Map("response" -> "done")).toFuture
  }

  /**
   * Update information regarding user product in the shopping cart
   *
   * curl -X POST http://localhost:7070/api/cart/products/{product_id} -d token={token} -d quantity={quantity.?}
   */
  post("/api/cart/products/:productId") { request =>
    val userId = UsersModel.getByToken(request.params.getOrElse("token", null))
    val productId = request.routeParams.get("productId").get.toInt
    val quantity = request.params.getInt("quantity")
    val product = UserCartModel.getUserProduct(userId, productId)

    if (product.isEmpty) throw new Exception("You can not update unexisting product!")
    else UserCartModel.updateProductQuantity(userId, productId, quantity)

    render.json(Map("response" -> "done")).toFuture
  }

  /**
   * Remove item from user's cart
   *
   * curl -i -X DELETE -G http://localhost:7070/api/cart/products/{product_id} -d token={token}
   */
  delete("/api/cart/products/:productId") { request =>
    val userId = getUserId(request)
    val productId = getProductId(request)

    UserCartModel remove (userId, productId) match {
      case 1 => render.status(204).toFuture
      case _ => throw new BadRequest("trying to remove product from user's shopping cart that is not there!")
    }
  }
}
