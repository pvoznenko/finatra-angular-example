package com.acme.ShoppingCart.exception

case class Conflict(message: String) extends Exception(message)