package com.acme.ShoppingCart.dao

import com.acme.ShoppingCart.dto.User
import scala.slick.driver.H2Driver.simple._
import com.acme.ShoppingCart.database.UsersDatabase._

object UsersDAO {
  def add(token: String) = users += User(token)

  def getAll(limit: Option[Int]) = users.take(limit.getOrElse(10)).run

  def getUserByToken(token: String) = users.filter(_.token === token).run
}