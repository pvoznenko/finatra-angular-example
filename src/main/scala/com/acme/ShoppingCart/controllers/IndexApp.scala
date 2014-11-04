package com.acme.ShoppingCart.controllers

import com.acme.ShoppingCart.exception._

class IndexApp extends ResponseController {

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
      case Some(error: IllegalArgumentException) => renderResponseErrorWithCustomMessage(error, 400, "Illegal Argument!")
      case Some(error: BadRequestException) => renderResponseError(error, 400, "Bad Request!")
      case Some(error: UnauthorizedException) => renderResponseErrorWithCustomMessage(error, 401, "Not Authorized!")
      case Some(error: ConflictException) => renderResponseError(error, 409, "ConflictException!")
      case Some(error: NotFoundException) => renderResponseError(error, 404, "Not Found!")
      case Some(error: UnsupportedOperationException) =>
        val message = "Unsupported Type!"
        log.error(error.toString, message)
        render.status(415).plain(message).toFuture
      case _ =>
        log.error(request.error.toString, "Something went wrong!")
        render.status(500).json(Map("error" -> "Something went wrong!")).toFuture
    }
  }
}
