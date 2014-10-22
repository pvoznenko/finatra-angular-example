package com.acme.ShoppingCart.model

import scala.slick.driver.H2Driver.simple._
import com.acme.ShoppingCart.model.database.{Products, DB, UserCart, Cart}

object UserCartModel {
  val userCart = TableQuery[UserCart]

  DB.connection.withSession{ implicit session => userCart.ddl.create }

  def add(userId: Int, productId: Int) = DB.connection.withSession{ implicit session => userCart += Cart(userId, productId, 1) }

  def getUserProducts(userId: Int) = DB.connection.withSession { implicit session =>
    val q = for {
      uc <- userCart if uc.userId === userId
      p <- TableQuery[Products] if uc.productId === p.id
    } yield (p.id, p.name, p.price, uc.quantity)

    q.list.map {case (id, name, price, quantity) => Map("id" -> id, "name" -> name, "price" -> price, "quantity" -> quantity)}
  }

  def getUserProduct(userId: Int, productId: Int) = DB.connection.withSession { implicit session => getByUserAndProduct(userId, productId).run }

  def incProductQuantity(userId: Int, productId: Int) = DB.connection.withSession { implicit session =>
    val query = getByUserAndProduct(userId, productId).map(_.quantity)
    val quantity = query.firstOption.getOrElse(-1)

    if (quantity < 0) quantity
    else query.update(quantity + 1)
  }

  def remove(userId: Int, productId: Int) = DB.connection.withSession { implicit session => getByUserAndProduct(userId, productId).delete }

  private[this] def getByUserAndProduct(userId: Int, productId: Int) = userCart.filter(_.userId === userId).filter(_.productId === productId)
}