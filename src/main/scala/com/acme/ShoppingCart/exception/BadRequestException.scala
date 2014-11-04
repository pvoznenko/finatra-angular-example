package com.acme.ShoppingCart.exception

case class BadRequestException(message: String) extends Exception(message)