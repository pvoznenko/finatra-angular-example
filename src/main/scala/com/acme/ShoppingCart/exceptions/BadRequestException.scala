package com.acme.ShoppingCart.exceptions

case class BadRequestException(message: String) extends Exception(message)