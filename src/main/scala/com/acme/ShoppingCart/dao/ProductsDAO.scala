package com.acme.ShoppingCart.dao

import com.acme.ShoppingCart.database.ProductsDatabase._
import scala.slick.driver.H2Driver.simple._

object ProductsDAO {
  def getAll(limit: Option[Int]) = products.take(limit.getOrElse(10)).run

  def getProductById(id: Int) = products.filter(_.id === id).run
}