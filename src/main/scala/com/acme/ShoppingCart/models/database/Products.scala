package com.acme.ShoppingCart.models.database

import scala.slick.driver.H2Driver.simple._

case class Product(name: String, price: Int, id: Option[Int] = None)

class Products(tag: Tag) extends Table[Product](tag, "PRODUCTS") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME", O.NotNull)
  def price = column[Int]("PRICE", O.NotNull)
  def * = (name, price, id.?) <> (Product.tupled, Product.unapply)
}