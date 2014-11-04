package com.acme.ShoppingCart.controllers.Api

import com.acme.ShoppingCart.controllers.ResponseController
import com.acme.ShoppingCart.exception.{NotFoundException, ConflictException}
import com.acme.ShoppingCart.models.UserCartModel
import com.acme.ShoppingCart.traits.{UsersTrait, ProductsTrait, UserCartTrait}

class CartProductsApi extends ResponseController with UsersTrait with ProductsTrait with UserCartTrait {

  /**
   * Get all products for user
   *
   * curl -i -X GET -G http://localhost:7070/api/cart/products -H token:{token}
   */
  get("/api/cart/products")(checkRequestType(_) { request =>
    val userId = getUserId(request)
    val products = UserCartModel getUserProducts userId

    render.json(products).toFuture
  })

  /**
   * Add new product to the user's shopping cart
   *
   * curl -i -X PUT http://localhost:7070/api/cart/products/{product_id} -H token:{token}
   */
  put("/api/cart/products/:productId")(checkRequestType(_) { request =>
    val userId = getUserId(request)
    val productId = getProductId(request)

    isProductInUserCart(productId, userId) match {
      case false =>
        UserCartModel add (userId, productId)
        val response = Map("rel" -> ("/api/cart/products/" ++ productId.toString))
        render.status(201).json(response).toFuture

      case _ => throw new ConflictException("ProductsTrait is already in user's cart!")
    }
  })

  /**
   * Update information regarding user product in the shopping cart
   *
   * curl -i -X PUT http://localhost:7070/api/cart/products/{product_id}/quantity/{quantity} -H token:{token}
   */
  put("/api/cart/products/:productId/quantity/:quantity")(checkRequestType(_) { request =>
    val userId = getUserId(request)
    val productId = getProductId(request)
    val quantity = getProductQuantity(request)

    isProductInUserCart(productId, userId) match {
      case true =>
        UserCartModel updateProductQuantity (userId, productId, quantity)
        render.status(204).toFuture

      case _ => throw new NotFoundException("Product should be in user's cart!")
    }
  })

  /**
   * Remove item from user's cart
   *
   * curl -i -X DELETE http://localhost:7070/api/cart/products/{product_id} -H token:{token}
   */
  delete("/api/cart/products/:productId")(checkRequestType(_) { request =>
    val userId = getUserId(request)
    val productId = getProductId(request)

    UserCartModel remove (userId, productId) match {
      case 1 => render.status(204).toFuture
      case _ => throw new NotFoundException("trying to remove product from user's shopping cart that is not there!")
    }
  })
}
