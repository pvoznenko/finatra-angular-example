package com.acme.ShoppingCart.dao

import com.acme.ShoppingCart.dto.User
import scala.slick.driver.H2Driver.simple._
import com.acme.ShoppingCart.database.UsersDatabase._
import com.acme.ShoppingCart.DB

object UsersDAO {
  def add(token: String) = DB.connection.withSession { implicit session =>
    users += User(token)
  }

  def getAll(limit: Option[Int]) = DB.connection.withSession { implicit session =>
    users.take(limit.getOrElse(10)).run
  }

  def getUserByToken(token: String) = DB.connection.withSession { implicit session =>
    users.filter(_.token === token).run
  }
}