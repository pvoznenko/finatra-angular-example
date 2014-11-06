package com.acme.ShoppingCart

import scala.slick.driver.H2Driver.simple._
import com.typesafe.config._

object DB {
  val config = ConfigFactory.load()
  val url = config.getString("db.url") ++ config.getString("api.version") ++ ";DB_CLOSE_DELAY=-1"
  val connection = Database.forURL(url, driver = config.getString("db.driver"))
}
