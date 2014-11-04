package com.acme.ShoppingCart.controllers.Api

import com.acme.ShoppingCart.exception.{NotFound, Conflict}
import com.acme.ShoppingCart.models.UserCartModel
import com.acme.ShoppingCart.traits.{Users, Products, UserCart}
import com.twitter.finatra.Controller

class CartProductsApi extends Controller with Users with Products with UserCart {

  /**
   * Get all products for user
   *
   * curl -X GET -G http://localhost:7070/api/cart/products -H token:{token}
   */
  get("/api/cart/products") { request =>
    val userId = getUserId(request)
    val products = UserCartModel getUserProducts userId

    render.json(products).toFuture
  }

  /**
   * Add new product to the user's shopping cart
   *
   * curl -X PUT http://localhost:7070/api/cart/products/{product_id} -H token:{token}
   */
  put("/api/cart/products/:productId") { request =>
    val userId = getUserId(request)
    val productId = getProductId(request)

    isProductInUserCart(productId, userId) match {
      case false =>
        UserCartModel add (userId, productId)
        render.status(201).json(Map("rel" -> ("/api/cart/products/" ++ productId.toString))).toFuture

      case _ => throw new Conflict("Products is already in user's cart!")
    }
  }

  /**
   * Update information regarding user product in the shopping cart
   *
   * curl -X PUT http://localhost:7070/api/cart/products/{product_id}/quantity/{quantity} -H token:{token}
   */
  put("/api/cart/products/:productId/quantity/:quantity") { request =>
    val userId = getUserId(request)
    val productId = getProductId(request)
    val quantity = getProductQuantity(request)

    isProductInUserCart(productId, userId) match {
      case true =>
        val data = UserCartModel updateProductQuantity (userId, productId, quantity)
        render.status(204).json(Map("data" -> data)).toFuture

      case _ => throw new NotFound("Product should be in user's cart!")
    }
  }

  /**
   * Remove item from user's cart
   *
   * curl -i -X DELETE -G http://localhost:7070/api/cart/products/{product_id} -H token:{token}
   */
  delete("/api/cart/products/:productId") { request =>
    val userId = getUserId(request)
    val productId = getProductId(request)

    UserCartModel remove (userId, productId) match {
      case 1 => render.status(204).toFuture
      case _ => throw new NotFound("trying to remove product from user's shopping cart that is not there!")
    }
  }
}
