package com.acme.ShoppingCart

import scala.slick.driver.H2Driver.simple._
import com.typesafe.config._

object DB {
  val config = ConfigFactory.load()
  val connection = Database.forURL(config.getString("db.url"), driver = config.getString("db.driver"))
}
