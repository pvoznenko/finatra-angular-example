package com.acme.ShoppingCart.controllers

import com.twitter.finatra.Controller
import com.acme.ShoppingCart.exception.{Conflict, BadRequest, Unauthorized}

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
      case Some(e: Unauthorized) =>
        val message = "Not Authorized!"
        log.error(request.error.toString, message)
        render.status(401).plain(message).toFuture

      case Some(e: IllegalArgumentException) =>
        val message = "Illegal Argument!"
        log.error(request.error.toString, message)
        render.status(400).plain(message).toFuture

      case Some(e: BadRequest) =>
        val message = if (e.getMessage.length > 0) e.getMessage else "Bad Request!"
        log.error(request.error.toString, message)
        render.status(400).plain(message).toFuture

      case Some(e: Conflict) =>
        val message = if (e.getMessage.length > 0) e.getMessage else "Conflict!"
        log.error(request.error.toString, message)
        render.status(409).plain(message).toFuture

      case _ =>
        log.error(request.error.toString, "Something went wrong!")
        render.status(500).plain("Something went wrong!").toFuture
    }
  }
}
