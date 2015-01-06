package com.acme.ShoppingCart.traits

trait ResponseTrait {
  def mapCreateResponse(id: Int, userId: Int, productId: Int) = Map(
    "id" -> id,
    "userId" -> userId,
    "productId" -> productId,
    "quantity" -> 1,
    "links" -> Map(
      "rel" -> "self",
      "href" -> ("/api/cart/products/" ++ productId.toString)
    ))
}
