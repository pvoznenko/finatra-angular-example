package com.acme.ShoppingCart.database

import com.acme.ShoppingCart.DB
import com.acme.ShoppingCart.dto.Cart
import scala.slick.driver.H2Driver.simple._

object UserCartDatabase {
  val userCart = TableQuery[UserCart]

  implicit val session = DB.connection.createSession()

  userCart.ddl.create
}

class UserCart(tag: Tag) extends Table[Cart](tag, "CART") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def userId = column[Int]("USER_ID", O.NotNull)
  def productId = column[Int]("PRODUCT_ID", O.NotNull)
  def quantity = column[Int]("QUANTITY", O.NotNull)
  def * = (userId, productId, quantity, id.?) <> (Cart.tupled, Cart.unapply)
  def customer = foreignKey("CUSTOMER_FK", userId, TableQuery[Users])(_.id)
  def product = foreignKey("PRODUCT_FK", productId, TableQuery[Products])(_.id)
  def userProduct = index("user_product", (userId, productId), unique = true)
}