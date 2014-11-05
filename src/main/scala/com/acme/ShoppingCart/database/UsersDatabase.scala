package com.acme.ShoppingCart.database

import com.acme.ShoppingCart.DB
import com.acme.ShoppingCart.dto.User
import scala.slick.driver.H2Driver.simple._

object UsersDatabase {
  val users = TableQuery[Users]

  implicit val session = DB.connection.createSession()

  users.ddl.create
}

class Users(tag: Tag) extends Table[User](tag, "USERS") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def token = column[String]("TOKEN", O.NotNull)
  def * = (token, id.?) <> (User.tupled, User.unapply)
}