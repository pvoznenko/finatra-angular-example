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

  error { request =>
    request.error match {
      case Some(error: BadRequestException) => renderResponseError(error, 400, "Bad Request!")
      case Some(error: UnauthorizedException) => renderResponseErrorWithCustomMessage(error.toString, 401, "Not Authorized!")
      case Some(error: NotFoundException) => renderResponseError(error, 404, "Not Found!")
      case Some(error: UnsupportedOperationException) => renderResponseErrorWithCustomMessage(error.toString, 406, "No matching accepted Response format could be determined!")
      case Some(error: ConflictException) => renderResponseError(error, 409, "ConflictException!")
      case Some(error: IllegalArgumentException) => renderResponseErrorWithCustomMessage(error.toString, 422, "Illegal Argument!")
      case _ => renderResponseErrorWithCustomMessage(request.error.toString, 500, "Something went wrong!")
    }
  }
}
