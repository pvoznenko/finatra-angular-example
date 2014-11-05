package com.acme.ShoppingCart.traits

import com.acme.ShoppingCart.dao.UserCartDAO

trait UserCartTrait {
  def isProductInUserCart(productId: Int, userId: Int) =
    UserCartDAO getUserProduct (userId, productId) match {
      case x +: xs => true
      case _ => false
    }
}
