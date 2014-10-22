package com.acme.ShoppingCart.model

import scala.slick.driver.H2Driver.simple._
import com.acme.ShoppingCart.model.database.{DB, Products, Product}

object ProductsModel {
  val products = TableQuery[Products]

  DB.connection.withSession { implicit session =>
    products.ddl.create

    products += Product("Mac Book Retina 15'", 1299)
    products += Product("Nexus 6", 649)
    products += Product("Surface", 750)
  }

  def getAll = DB.connection.withSession { implicit session => products.run }
}