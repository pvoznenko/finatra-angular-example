package com.acme.ShoppingCart.controllers.Api

import com.acme.ShoppingCart.controllers.ResponseController
import com.acme.ShoppingCart.exceptions.{NotFoundException, ConflictException}
import com.acme.ShoppingCart.dao.UserCartDAO
import com.acme.ShoppingCart.traits.{ResponseTrait, UsersTrait, ProductsTrait, UserCartTrait}

class CartProductsApi extends ResponseController with UsersTrait with ProductsTrait with UserCartTrait with ResponseTrait {

  /**
   * Get all products for user
   *
   * curl -i -H Accept:application/json -X GET -G http://localhost:7070/api/cart/products -H token:{token}
   */
  get("/api/cart/products")(checkRequestType(_) { request =>
    val userId = getUserId(request)
    val products = UserCartDAO getUserProducts userId

    renderResponse(request, render, None, Some(products))
  })

  /**
   * Add new product to the user's shopping cart
   *
   * curl -i -H Accept:application/json -X PUT http://localhost:7070/api/cart/products/{productId} -H token:{token}
   */
  put("/api/cart/products/:productId")(checkRequestType(_) { request =>
    val userId = getUserId(request)
    val productId = getProductId(request)

    if (isProductInUserCart(productId, userId))
      throw new ConflictException("Product is already in user's cart!")
    else {
      val rowId = UserCartDAO add (userId, productId)
      val response = mapCreateResponse(rowId, userId, productId)

      renderResponse(request, render, Some(201), Some(response))
    }
  })

  /**
   * Update information regarding user product in the shopping cart
   *
   * curl -i -H Accept:application/json -X PUT http://localhost:7070/api/cart/products/{productId}/quantity/{quantity} -H token:{token}
   */
  put("/api/cart/products/:productId/quantity/:quantity")(checkRequestType(_) { request =>
    val userId = getUserId(request)
    val productId = getProductId(request)
    val quantity = getProductQuantity(request)

    if (isProductInUserCart(productId, userId)) {
      UserCartDAO updateProductQuantity (userId, productId, quantity)
      renderResponse(request, render, Some(204))
    } else throw new NotFoundException("Product should be in user's cart!")
  })

  /**
   * Remove item from user's cart
   *
   * curl -i -H Accept:application/json -X DELETE http://localhost:7070/api/cart/products/{productId} -H token:{token}
   */
  delete("/api/cart/products/:productId")(checkRequestType(_) { request =>
    val userId = getUserId(request)
    val productId = getProductId(request)

    val removedRows = UserCartDAO remove (userId, productId)

    if (removedRows > 0) renderResponse(request, render, Some(204))
    else throw new NotFoundException("trying to remove product from user's shopping cart that is not there!")
  })
}
