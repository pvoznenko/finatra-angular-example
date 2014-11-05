package com.acme.ShoppingCart.dto

case class Product(name: String, price: Int, id: Option[Int] = None)
