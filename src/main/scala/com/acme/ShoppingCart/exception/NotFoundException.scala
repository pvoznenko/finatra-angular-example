package com.acme.ShoppingCart.exception

case class NotFoundException(message: String) extends Exception(message)