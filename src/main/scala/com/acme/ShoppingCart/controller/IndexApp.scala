package com.acme.ShoppingCart.controller

import com.twitter.finatra.Controller

class IndexApp extends Controller {
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
}
