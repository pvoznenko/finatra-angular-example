package com.acme.ShoppingCart.controller

import com.twitter.finatra.Controller
import com.acme.ShoppingCart.exception.Unauthorized

class IndexApp extends Controller {
  /**
   * Single page application on AngularJS
   *
   * curl http://localhost:7070/
   */
  get("/") { request =>
    render.static("index.html").toFuture
  }

  /**
   * Custom 404s
   *
   * curl http://localhost:7070/notfound
   */
  notFound { request =>
    render.status(404).plain("not found").toFuture
  }

  error { request =>
    request.error match {
      case Some(e:Unauthorized) =>
        render.status(401).plain("Not Authorized!").toFuture
      case _ =>
        render.status(500).plain("Something went wrong!").toFuture
    }
  }
}
