package com.acme.ShoppingCart.exceptions

case class ConflictException(message: String) extends Exception(message)