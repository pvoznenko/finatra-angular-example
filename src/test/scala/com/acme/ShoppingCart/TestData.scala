package com.acme.ShoppingCart

object TestData {
  val products = List(
    Map("name" -> "Mac Book Retina 15'", "price" -> 1299.0, "id" -> 1.0),
    Map("name" -> "Nexus 6", "price" -> 649.0, "id" -> 2.0),
    Map("name" -> "Surface", "price" -> 750.0, "id" -> 3.0))

  val addedProduct = Map("rel" -> "/api/cart/products/1")

  val firstProduct = List(Map("id" -> 1.0, "name" -> "Mac Book Retina 15'", "price" -> 1299.0, "quantity" -> 1.0))
}
