package com.acme.ShoppingCart.controllers

import com.twitter.finatra.ContentType.Json
import com.twitter.finatra.{Controller, Logging}
import com.twitter.finatra.{Request, ResponseBuilder}
import com.twitter.util.Future

abstract class ResponseController extends Controller with Logging {
  def renderResponseErrorWithCustomMessage(error: Exception, responseCode: Int, message: String) = {
    log.error(error.toString, message)
    render.status(responseCode).json(Map("error" -> message)).toFuture
  }

  def renderResponseError(error: Exception, responseCode: Int, defaultMessage: String) = {
    val message = if (error.getMessage.length > 0) error.getMessage else defaultMessage
    renderResponseErrorWithCustomMessage(error, responseCode, message)
  }

  def renderResponse(request: Request, render: ResponseBuilder) =
    respondTo(request) {
      case _:Json => render.toFuture
      case _ => throw new UnsupportedOperationException
    }

  def checkRequestType(request: Request)(callback: (Request) => Future[ResponseBuilder]) =
    request.headers().get("Accept") match {
      case "*/*" | "application/json" => callback(request)
      case _ => throw new UnsupportedOperationException
    }
}
