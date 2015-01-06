package com.acme.ShoppingCart.traits

import com.acme.ShoppingCart.exceptions.{UnauthorizedException, NotFoundException, BadRequestException}
import com.acme.ShoppingCart.models.{UsersModel, ProductsModel}
import scala.collection.Map
import scala.util.{Failure, Success}

sealed trait ParametersValidationTrait {
  def tryGetParameter(params: Map[String, String], paramKey: String) = params.get(paramKey) match {
    case Some(param) => Success(param)
    case _ => Failure(BadRequestException("Parameter '" ++ paramKey ++ "' is required!"))
  }
}

trait ProductsTrait extends ParametersValidationTrait {
  def tryGetProductId(availableParams: Map[String, String]) = tryGetParameter(availableParams, "productId") match {
    case Success(productId) => ProductsModel getProductById productId.toInt match {
      case x +: xs => Success(x.id.get)
      case _ => Failure(NotFoundException("Product with provided id '" ++ productId ++ "' is not exist!"))
    }
    case Failure(error) => Failure(error)
  }

  def tryGetProductQuantity(availableParams: Map[String, String]) = tryGetParameter(availableParams, "quantity") match {
    case Success(quantity) => quantity.toInt match {
      case amount if amount < 0 => Failure(new IllegalArgumentException)
      case amount => Success(amount)
    }
    case Failure(error) => Failure(error)
  }
}

trait UsersTrait extends ParametersValidationTrait {
  def tryGetUserId(availableParams: Map[String, String]) = tryGetParameter(availableParams, "token") match {
    case Success(token) => UsersModel getUserByToken token match {
      case x +: xs => Success(x.id.get)
      case _ => Failure(UnauthorizedException())
    }
    case Failure(error) => error match {
      case exception: BadRequestException => Failure(UnauthorizedException())
      case _ => Failure(error)
    }
  }
}