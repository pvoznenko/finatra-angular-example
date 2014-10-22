package com.acme.ShoppingCart.controller

import com.twitter.finatra.Controller

class IndexApp extends Controller {
  get("/") { request =>
    render.static("index.html").toFuture
  }
}
