package com.acme.ShoppingCart.controllers.Api

import com.acme.ShoppingCart.exception.{BadRequest, Unauthorized}
import com.acme.ShoppingCart.models.{ProductsModel, UsersModel, UserCartModel}
import com.twitter.finatra.Controller
import com.twitter.finatra.Request

class CartProductsApi extends Controller {

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

    val removedRowsCount = UserCartModel remove (userId, productId)

    if (removedRowsCount <= 0) throw new BadRequest("trying to remove product from user's shopping cart that is not there!")
    else render.status(204).toFuture
  }

  private [this] def getUserId(request: Request) = {
    def getAuthTokenParam(request: Request) = {
      val token = request.params.getOrElse("token", null)

      if (token == null) throw new BadRequest("Parameter 'token' is required!")
      else token
    }

    val token = getAuthTokenParam(request)
    val users = UsersModel getUserByToken token

    if (users.isEmpty) throw new Unauthorized
    else users.head.id.get
  }

  private [this] def getProductId(request: Request) = {
    val productId = request.routeParams.get("productId").get.toInt
    val products = ProductsModel getProductById productId

    if (products.isEmpty) throw new BadRequest("Product with provided id '" ++ productId.toString ++ "' is not exist!")
    else products.head.id.get
  }
}
