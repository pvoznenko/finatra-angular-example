package com.acme.ShoppingCart

import com.acme.ShoppingCart.controller.IndexApp
import com.acme.ShoppingCart.controller.Api.{ProductApi, CartApi, UserApi}
import com.twitter.finatra._
import com.twitter.finatra.ContentType._

object App extends FinatraServer {
  class ExampleApp extends Controller {
    delete("/photos") { request =>
      render.plain("deleted!").toFuture
    }

    /**
     * Route parameters
     *
     * curl http://localhost:7070/user/dave => "hello dave"
     */
    get("/user/:username") { request =>
      val username = request.routeParams.getOrElse("username", "default_user")
      render.plain("hello " + username).toFuture
    }

    /**
     * Setting Headers
     *
     * curl -I http://localhost:7070/headers => "Foo:Bar"
     */
    get("/headers") { request =>
      render.plain("look at headers").header("Foo", "Bar").toFuture
    }

    /**
     * Rendering json
     *
     * curl -I http://localhost:7070/data.json => "{foo:bar}"
     */
    get("/data.json") { request =>
      render.json(Map("foo" -> "bar")).toFuture
    }

    /**
     * Query params
     *
     * curl http://localhost:7070/search?q=foo => "no results for foo"
     */
    get("/search") { request =>
      request.params.get("q") match {
        case Some(q) => render.plain("no results for "+ q).toFuture
        case None    => render.plain("query param q needed").status(500).toFuture
      }
    }

    /**
     * Redirects
     *
     * curl http://localhost:7070/redirect
     */
    get("/redirect") { request =>
      redirect("http://localhost:7070/", permanent = true).toFuture
    }

    /**
     * Uploading files
     *
     * curl -F avatar=@/path/to/img http://localhost:7070/profile
     */
    post("/profile") { request =>
      request.multiParams.get("avatar").map { avatar =>
        println("content type is " + avatar.contentType)
        avatar.writeToFile("/tmp/avatar") //writes uploaded avatar to /tmp/avatar
      }
      render.plain("ok").toFuture
    }

    options("/some/resource") { request =>
      render.plain("usage description").toFuture
    }

    /**
     * Rendering views
     *
     * curl http://localhost:7070/template
     */
    class AnView extends View {
      val template = "an_view.mustache"
      val some_val = "random value here"
    }

    get("/template") { request =>
      val anView = new AnView
      render.view(anView).toFuture
    }


    /**
     * Custom Error Handling
     *
     * curl http://localhost:7070/error
     */
    get("/error")   { request =>
      1234/0
      render.plain("we never make it here").toFuture
    }

    /**
     * Custom Error Handling with custom Exception
     *
     * curl http://localhost:7070/unauthorized
     */
    class Unauthorized extends Exception

    get("/unauthorized") { request =>
      throw new Unauthorized
    }

    error { request =>
      request.error match {
        case Some(e:ArithmeticException) =>
          render.status(500).plain("whoops, divide by zero!").toFuture
        case Some(e:Unauthorized) =>
          render.status(401).plain("Not Authorized!").toFuture
        case Some(e:UnsupportedMediaType) =>
          render.status(415).plain("Unsupported Media Type!").toFuture
        case _ =>
          render.status(500).plain("Something went wrong!").toFuture
      }
    }


    /**
     * Custom 404s
     *
     * curl http://localhost:7070/notfound
     */
    notFound { request =>
      render.status(404).plain("not found yo").toFuture
    }

    /**
     * Arbitrary Dispatch
     *
     * curl http://localhost:7070/go_home
     */
    get("/go_home") { request =>
      route.get("/")
    }

    get("/search_for_dogs") { request =>
      route.get("/search", Map("q" -> "dogs"))
    }

    get("/delete_photos") { request =>
      route.delete("/photos")
    }

    get("/gif") { request =>
      render.static("/dealwithit.gif").toFuture
    }

    /**
     * Dispatch based on Content-Type
     *
     * curl http://localhost:7070/blog/index.json
     * curl http://localhost:7070/blog/index.html
     */
    get("/blog/index.:format") { request =>
      respondTo(request) {
        case _:Html => render.html("<h1>Hello</h1>").toFuture
        case _:Json => render.json(Map("value" -> "hello")).toFuture
      }
    }

    /**
     * Also works without :format route using browser Accept header
     *
     * curl -H "Accept: text/html" http://localhost:7070/another/page
     * curl -H "Accept: application/json" http://localhost:7070/another/page
     * curl -H "Accept: foo/bar" http://localhost:7070/another/page
     */

    get("/another/page") { request =>
      respondTo(request) {
        case _:Html => render.plain("an html response").toFuture
        case _:Json => render.plain("an json response").toFuture
        case _:All => render.plain("default fallback response").toFuture
      }
    }

    /**
     * Metrics are supported out of the box via Twitter's Ostrich library.
     * More details here: https://github.com/twitter/ostrich
     *
     * curl http://localhost:7070/slow_thing
     *
     * By default a stats server is started on 9990:
     *
     * curl http://localhost:9990/stats.txt
     *
     */

    get("/slow_thing") { request =>
      stats.counter("slow_thing").incr
      stats.time("slow_thing time") {
        Thread.sleep(100)
      }
      render.plain("slow").toFuture
    }

  }

  register(new IndexApp())
  register(new UserApi())
  register(new ProductApi())
  register(new CartApi())

  register(new ExampleApp())
}
