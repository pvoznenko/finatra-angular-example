package com.acme.ShoppingCart

import com.typesafe.config._

object API {
  val config = ConfigFactory.load()
  val version = config.getString("api.version")
  val baseUrl = "/api/v" ++ version

  def getBaseUrl = baseUrl
}
