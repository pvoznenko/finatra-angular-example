package com.acme.ShoppingCart.dto

case class Cart(userId: Int, productId: Int, quantity: Int, id: Option[Int] = None)
