package com.acme.ShoppingCart.exceptions

sealed abstract class BaseException(message: String, val defaultMessage: String, val httpCode: Int) extends Exception(message)

case class BadRequestException(message: String, override val defaultMessage: String = "Bad Request!", override val httpCode: Int = 400) extends BaseException(message, defaultMessage, httpCode)
case class UnauthorizedException(message: String = "", override val defaultMessage: String = "Not Authorized!", override val httpCode: Int = 401) extends BaseException(message, defaultMessage, httpCode)
case class NotFoundException(message: String, override val defaultMessage: String = "Not Found!", override val httpCode: Int = 404) extends BaseException(message, defaultMessage, httpCode)
case class ConflictException(message: String, override val defaultMessage: String = "ConflictException!", override val httpCode: Int = 409) extends BaseException(message, defaultMessage, httpCode)