package com.acme.ShoppingCart

import scala.slick.driver.H2Driver.simple._

object DB {
  val connection = Database.forURL("jdbc:h2:mem:shoppingCart;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
}
