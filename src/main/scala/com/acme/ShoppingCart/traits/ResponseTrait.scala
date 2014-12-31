package com.acme.ShoppingCart.traits

import com.acme.ShoppingCart.exceptions.{BadRequestException, UnauthorizedException}
import com.acme.ShoppingCart.models.UsersModel
import com.twitter.finatra.Request

trait ResponseTrait {
  def mapCreateResponse(id: Int, userId: Int, productId: Int) =
    Map(
      "id" -> id,
      "userId" -> userId,
      "productId" -> productId,
      "quantity" -> 1,
      "links" -> Map(
        "rel" -> "self",
        "href" -> ("/api/cart/products/" ++ productId.toString)
      ))
}
