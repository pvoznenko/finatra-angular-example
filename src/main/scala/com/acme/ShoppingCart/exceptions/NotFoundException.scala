package com.acme.ShoppingCart.exceptions

case class NotFoundException(message: String) extends Exception(message)