package com.acme.ShoppingCart.database

import com.acme.ShoppingCart.DB
import com.acme.ShoppingCart.dto.Product
import scala.slick.driver.H2Driver.simple._

object ProductsDatabase {
  val products = TableQuery[Products]

  implicit val session = DB.connection.createSession()

  products.ddl.create

  products += Product("Mac Book Retina 15'", 1299)
  products += Product("Nexus 6", 649)
  products += Product("Surface", 750)
}

class Products(tag: Tag) extends Table[Product](tag, "PRODUCTS") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME", O.NotNull)
  def price = column[Int]("PRICE", O.NotNull)
  def * = (name, price, id.?) <> (Product.tupled, Product.unapply)
}