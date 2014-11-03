package com.acme.ShoppingCart.exception

case class BadRequest(message: String) extends Exception(message)