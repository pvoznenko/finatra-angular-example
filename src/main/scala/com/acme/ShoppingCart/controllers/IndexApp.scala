package com.acme.ShoppingCart.controllers

import com.acme.ShoppingCart.exceptions._

class IndexApp extends ResponseController {

  /**
   * Single page application on AngularJS
   *
   * curl http://localhost:7070/
   */
  get("/") { request =>
    render.static("index.html").toFuture
  }

  /**
   * Error handler, to show correct response to the client
   */
  error { request =>
    request.error match {
      case Some(error: UnauthorizedException) => renderResponseErrorWithCustomMessage(error.toString, error.httpCode, error.defaultMessage) // 401
      case Some(error: BaseException) => renderResponseError(error) // 400, 404, 409
      case Some(error: UnsupportedOperationException) => renderResponseErrorWithCustomMessage(error.toString, 406, "No matching accepted Response format could be determined!")
      case Some(error: IllegalArgumentException) => renderResponseErrorWithCustomMessage(error.toString, 422, "Illegal Argument!")
      case _ => renderResponseErrorWithCustomMessage(request.error.toString, 500, "Something went wrong!")
    }
  }
}
