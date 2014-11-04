package com.acme.ShoppingCart.controllers

import com.twitter.finatra.Controller
import com.acme.ShoppingCart.exception.{NotFound, Conflict, BadRequest, Unauthorized}

class IndexApp extends Controller {

  /**
   * Single page application on AngularJS
   *
   * curl http://localhost:7070/
   */
  get("/") { request =>
    render.static("index.html").toFuture
  }

  error { request =>
    request.error match {
      case Some(e: IllegalArgumentException) => responseWithMessage(e, 400, "Illegal Argument!")
      case Some(e: BadRequest) => response(e, 400, "Bad Request!")
      case Some(e: Unauthorized) => responseWithMessage(e, 401, "Not Authorized!")
      case Some(e: Conflict) => response(e, 409, "Conflict!")
      case Some(e: NotFound) => response(e, 404, "Not Found!")
      case _ =>
        log.error(request.error.toString, "Something went wrong!")
        render.status(500).json(Map("error" -> "Something went wrong!")).toFuture
    }
  }

  private [this] def responseWithMessage(error: Exception, responseCode: Int, message: String) = {
    log.error(error.toString, message)
    render.status(responseCode).json(Map("error" -> message)).toFuture
  }

  private [this] def response(error: Exception, responseCode: Int, defaultMessage: String) = {
    val message = if (error.getMessage.length > 0) error.getMessage else defaultMessage
    responseWithMessage(error, responseCode, message)
  }
}
