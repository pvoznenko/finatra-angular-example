package com.acme.ShoppingCart.model

import scala.slick.driver.H2Driver.simple._
import com.acme.ShoppingCart.model.database.{DB, Users, User}

object UsersModel {
  val users = TableQuery[Users]

  DB.connection.withSession{ implicit session => users.ddl.create }

  def add(token: String) = DB.connection.withSession{ implicit session => users += User(token) }

  def getAll = DB.connection.withSession { implicit session => users.list }

  def getByToken(token: String) = DB.connection.withSession { implicit session =>
    val userId = users.filter(_.token === token).map(_.id).firstOption.getOrElse(-1)

    if (userId < 0) throw new Exception("User not Authorized!")
    else userId
  }
}