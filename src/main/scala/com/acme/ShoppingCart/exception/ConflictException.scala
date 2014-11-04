package com.acme.ShoppingCart.exception

case class ConflictException(message: String) extends Exception(message)