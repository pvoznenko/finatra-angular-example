package com.acme.ShoppingCart.models.database

import com.acme.ShoppingCart.DB

import scala.slick.driver.H2Driver.simple._

object ProductsDatabase {
  val products = TableQuery[Products]

  DB.connection.withSession { implicit session =>
    products.ddl.create

    products += ProductEntity("Mac Book Retina 15'", 1299)
    products += ProductEntity("Nexus 6", 649)
    products += ProductEntity("Surface", 750)
  }
}

case class ProductEntity(name: String, price: Int, id: Option[Int] = None)

class Products(tag: Tag) extends Table[ProductEntity](tag, "PRODUCTS") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME", O.NotNull)
  def price = column[Int]("PRICE", O.NotNull)
  def * = (name, price, id.?) <> (ProductEntity.tupled, ProductEntity.unapply)
}