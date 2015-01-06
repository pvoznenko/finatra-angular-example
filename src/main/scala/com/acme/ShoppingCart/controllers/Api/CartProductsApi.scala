package com.acme.ShoppingCart.controllers.Api

import com.acme.ShoppingCart.controllers.ResponseController
import com.acme.ShoppingCart.exceptions.{NotFoundException, ConflictException}
import com.acme.ShoppingCart.models.UserCartModel
import com.acme.ShoppingCart.traits.{ResponseTrait, UsersTrait, ProductsTrait}
import com.acme.ShoppingCart.API
import scala.util.{Try, Success, Failure}

class CartProductsApi extends ResponseController with UsersTrait with ProductsTrait with ResponseTrait {

  /**
   * Get all products for user
   *
   * curl -i -H Accept:application/json -X GET -G http://localhost:7070/api/v3/cart/products -H token:{token}
   */
  get(API.getBaseUrl ++ "/cart/products")(checkRequestType(_) { request =>
    (for {
      userId <- tryGetUserId(request.headerMap)
      products <- Try(UserCartModel getUserProducts userId)
    } yield {
      products
    }) match {
      case Failure(error) => throw error
      case Success(products) => renderResponse(request, render, None, Some(products))
    }
  })

  /**
   * Add new product to the user's shopping cart
   *
   * curl -i -H Accept:application/json -X PUT http://localhost:7070/api/v3/cart/products/{productId} -H token:{token}
   */
  put(API.getBaseUrl ++ "/cart/products/:productId")(checkRequestType(_) { request =>
    (for {
      userId <- tryGetUserId(request.headerMap)
      productId <- tryGetProductId(request.routeParams)
      check <- Try(if (UserCartModel isProductInUserCart(productId, userId)) throw new ConflictException("Product is already in user's cart!"))
      rowId <- Try(UserCartModel add (userId, productId))
    } yield {
      mapCreateResponse(rowId, userId, productId)
    }) match {
      case Failure(error) => throw error
      case Success(response) => renderResponse(request, render, Some(201), Some(response))
    }
  })

  /**
   * Update information regarding user product in the shopping cart
   *
   * curl -i -H Accept:application/json -X PUT http://localhost:7070/api/v3/cart/products/{productId}/quantity/{quantity} -H token:{token}
   */
  put(API.getBaseUrl ++ "/cart/products/:productId/quantity/:quantity")(checkRequestType(_) { request =>
    (for {
      userId <- tryGetUserId(request.headerMap)
      productId <- tryGetProductId(request.routeParams)
      quantity <- tryGetProductQuantity(request.routeParams)
      check <- Try(if (!(UserCartModel isProductInUserCart(productId, userId))) throw new NotFoundException("Product should be in user's cart!"))
      changedRows <- Try(UserCartModel updateProductQuantity(userId, productId, quantity))
    } yield {
      changedRows
    }) match {
      case Failure(error) => throw error
      case Success(_) => renderResponse(request, render, Some(204))
    }
  })

  /**
   * Remove item from user's cart
   *
   * curl -i -H Accept:application/json -X DELETE http://localhost:7070/api/v3/cart/products/{productId} -H token:{token}
   */
  delete(API.getBaseUrl ++ "/cart/products/:productId")(checkRequestType(_) { request =>
    (for {
      userId <- tryGetUserId(request.headerMap)
      productId <- tryGetProductId(request.routeParams)
      removedRows <- Try(UserCartModel remove (userId, productId))
      check <- Try(if (removedRows <= 0) throw new NotFoundException("trying to remove product from user's shopping cart that is not there!"))
    } yield {
      removedRows
    }) match {
      case Failure(error) => throw error
      case Success(_) => renderResponse(request, render, Some(204))
    }
  })
}
