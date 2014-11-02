package com.acme.ShoppingCart.models.database

import scala.slick.driver.H2Driver.simple._

case class User(token: String, id: Option[Int] = None)

class Users(tag: Tag) extends Table[User](tag, "USERS") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def token = column[String]("TOKEN", O.NotNull)
  def * = (token, id.?) <> (User.tupled, User.unapply)
}