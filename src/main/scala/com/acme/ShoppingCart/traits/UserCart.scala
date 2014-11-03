package com.acme.ShoppingCart.traits

import com.acme.ShoppingCart.models.UserCartModel

trait UserCart {
  def isProductInUserCart(productId: Int, userId: Int) =
    UserCartModel getUserProduct (userId, productId) match {
      case x +: xs => true
      case _ => false
    }
}
