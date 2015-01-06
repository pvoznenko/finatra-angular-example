package com.acme.ShoppingCart.controllers

import com.acme.ShoppingCart.exceptions.BaseException
import com.twitter.finatra.ContentType.Json
import com.twitter.finatra.{Controller, Logging}
import com.twitter.finatra.{Request, ResponseBuilder}
import com.twitter.util.Future

abstract class ResponseController extends Controller with Logging {

  def renderResponseErrorWithCustomMessage(error: String, responseCode: Int, message: String) = {
    val response = Map(
      "error" -> Map(
        "code" -> responseCode,
        "message" -> message
      ))

    log.error(error, message)
    render.status(responseCode).json(response).toFuture
  }

  def renderResponseError(error: BaseException) = {
    val message = if (error.getMessage.length > 0) error.getMessage else error.defaultMessage
    renderResponseErrorWithCustomMessage(error.toString, error.httpCode, message)
  }

  def renderResponse(request: Request, render: ResponseBuilder, status: Option[Int] = None, data: Option[Iterable[Any]] = None): Future[ResponseBuilder] =
    respondTo(request) {
      case _:Json =>
        status getOrElse None match {
          case code: Int => renderResponse(request, render.status(code), None, data)
          case _ =>
            data getOrElse None match {
              case response: Iterable[Any] => renderResponse(request, render.json(response), status)
              case _ => render.toFuture
            }
        }
      case _ => throw new UnsupportedOperationException
    }

  def checkRequestType(request: Request)(callback: (Request) => Future[ResponseBuilder]) =
    request.headers().get("Accept").split(",").map(_.trim) match {
      case array if array.contains("*/*") || array.contains("application/json") => callback(request)
      case _ => throw new UnsupportedOperationException
    }
}
