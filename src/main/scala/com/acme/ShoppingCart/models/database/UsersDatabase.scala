package com.acme.ShoppingCart.models.database

import com.acme.ShoppingCart.DB

import scala.slick.driver.H2Driver.simple._

object UsersDatabase {
  val users = TableQuery[Users]

  DB.connection.withSession { implicit session =>
    users.ddl.create
  }
}

case class UserEntity(token: String, id: Option[Int] = None)

class Users(tag: Tag) extends Table[UserEntity](tag, "USERS") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def token = column[String]("TOKEN", O.NotNull)
  def * = (token, id.?) <> (UserEntity.tupled, UserEntity.unapply)
}