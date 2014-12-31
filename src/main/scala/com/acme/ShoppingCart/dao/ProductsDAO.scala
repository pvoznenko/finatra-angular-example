package com.acme.ShoppingCart.dao

import com.acme.ShoppingCart.database.ProductsDatabase._
import scala.slick.driver.H2Driver.simple._
import com.acme.ShoppingCart.DB

object ProductsDAO {
  def getAll(limit: Option[Int]) = DB.connection.withSession { implicit session =>
    products.take(limit.getOrElse(10)).run
  }

  def getProductById(id: Int) = DB.connection.withSession { implicit session =>
    products.filter(_.id === id).run
  }
}