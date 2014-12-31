package com.acme.ShoppingCart.dao

import com.acme.ShoppingCart.dto.Cart
import scala.slick.driver.H2Driver.simple._
import com.acme.ShoppingCart.database.UserCartDatabase._
import com.acme.ShoppingCart.database.Products
import com.acme.ShoppingCart.DB

object UserCartDAO {
  def add(userId: Int, productId: Int) = DB.connection.withSession { implicit session =>
      (userCart returning userCart.map(_.id)) += Cart(userId, productId, 1)
    }

  def getUserProducts(userId: Int) = DB.connection.withSession { implicit session =>
    val query = for {
      userCart <- userCart if userCart.userId === userId
      product <- TableQuery[Products] if userCart.productId === product.id
    } yield (product.id, product.name, product.price, userCart.quantity)

    query.list.map { case (id, name, price, quantity) => Map("id" -> id, "name" -> name, "price" -> price, "quantity" -> quantity) }
  }

  def getUserProduct(userId: Int, productId: Int) = DB.connection.withSession { implicit session =>
    getByUserAndProduct(userId, productId).run
  }

  def updateProductQuantity(userId: Int, productId: Int, quantity: Int) = DB.connection.withSession { implicit session =>
    val query = getByUserAndProduct(userId, productId).map(_.quantity)
    query.update(quantity)
  }

  def remove(userId: Int, productId: Int) = DB.connection.withSession { implicit session =>
    getByUserAndProduct(userId, productId).delete
  }

  private[this] def getByUserAndProduct(userId: Int, productId: Int) = DB.connection.withSession { implicit session =>
    userCart.filter(_.userId === userId).filter(_.productId === productId)
  }
}