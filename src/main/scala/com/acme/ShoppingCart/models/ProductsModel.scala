package com.acme.ShoppingCart.models

import scala.slick.driver.H2Driver.simple._
import com.acme.ShoppingCart.models.database.{DB, Products, Product}

object ProductsModel {
  val products = TableQuery[Products]

  DB.connection.withSession { implicit session =>
    products.ddl.create

    products += Product("Mac Book Retina 15'", 1299)
    products += Product("Nexus 6", 649)
    products += Product("Surface", 750)
  }

  def getAll(limit: Option[Int]) = DB.connection.withSession { implicit session => products.take(limit.getOrElse(10)).run }
}