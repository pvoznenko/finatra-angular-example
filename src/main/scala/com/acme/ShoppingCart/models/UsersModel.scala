package com.acme.ShoppingCart.models

import scala.slick.driver.H2Driver.simple._
import com.acme.ShoppingCart.models.database.{DB, Users, User}
import com.acme.ShoppingCart.exception.Unauthorized

object UsersModel {
  val users = TableQuery[Users]

  DB.connection.withSession{ implicit session => users.ddl.create }

  def add(token: String) = DB.connection.withSession{ implicit session => users += User(token) }

  def getAll(limit: Option[Int]) = DB.connection.withSession{ implicit session => users.take(limit.getOrElse(10)).run }

  def getByToken(token: String) = DB.connection.withSession { implicit session =>
    val userId = users.filter(_.token === token).map(_.id).firstOption.getOrElse(-1)

    if (userId < 0) throw new Unauthorized
    else userId
  }

  def getUserByToken(token: String) = DB.connection.withSession { implicit session =>
    users.filter(_.token === token).run
  }
}