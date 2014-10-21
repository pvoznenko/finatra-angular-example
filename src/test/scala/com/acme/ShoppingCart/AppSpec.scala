package com.acme.ShoppingCart

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import com.twitter.finatra.test._
import com.twitter.finatra.FinatraServer
import com.acme.ShoppingCart._

class AppSpec extends FlatSpecHelper {

  val app = new App.ExampleApp
  override val server = new FinatraServer
  server.register(app)

  
  "GET /notfound" should "respond 404" in {
    get("/notfound")
    response.body   should equal ("not found yo")
    response.code   should equal (404)
  }

  "GET /error" should "respond 500" in {
    get("/error")
    response.body   should equal ("whoops, divide by zero!")
    response.code   should equal (500)
  }

  "GET /unauthorized" should "respond 401" in {
    get("/unauthorized")
    response.body   should equal ("Not Authorized!")
    response.code   should equal (401)
  }

  "GET /index.html" should "respond 200" in {
    get("/")
    response.body.contains("Finatra - The scala web framework") should equal(true)
    response.code should equal(200)
  }

  "GET /user/foo" should "responsd with hello foo" in {
    get("/user/foo")
    response.body should equal ("hello foo")
  }

  "GET /headers" should "respond with Foo:Bar" in {
    get("/headers")
    response.getHeader("Foo") should equal("Bar")
  }

  "GET /data.json" should """respond with {"foo":"bar"}""" in {
    get("/data.json")
    response.body should equal("""{"foo":"bar"}""")
  }

  "GET /search?q=foo" should "respond with no results for foo" in {
    get("/search?q=foo")
    response.body should equal("no results for foo")
  }

  "GET /redirect" should "respond with /" in {
    get("/redirect")
    response.body should equal("Redirecting to <a href=\"http://localhost:7070/\">http://localhost:7070/</a>.")
    response.code should equal(301)
  }

  "OPTIONS /some/resource" should "respond with usage description" in {
    options("/some/resource")
    response.body should equal("usage description")
  }

  "GET /template" should "respond with a rendered template" in {
    get("/template")
    response.body should equal("Your value is random value here")
  }

  "GET /blog/index.json" should "should have json" in {
    get("/blog/index.json")
    response.body should equal("""{"value":"hello"}""")
  }

  "GET /blog/index.html" should "should have html" in {
    get("/blog/index.html")
    response.body should equal("""<h1>Hello</h1>""")
  }

  "GET /blog/index.rss" should "respond in a 415" in {
    get("/blog/index.rss")
    response.code should equal(415)
  }

  "GET /go_home" should "render same as /" in {
    get("/go_home")
    response.body.contains("Finatra - The scala web framework") should equal(true)
    response.code should equal(200)
  }

  "GET /search_for_dogs" should "render same as /search?q=dogs" in {
    get("/search_for_dogs")
    response.code should equal(200)
    response.body should equal("no results for dogs")
  }

  "GET /delete_photos" should "render same as DELETE /photos" in {
    get("/delete_photos")
    response.code should equal(200)
    response.body should equal("deleted!")
  }

  "GET /gif" should "render dealwithit.gif" in {
    get("/gif")
    response.code should equal(200)
    response.originalResponse.getContent().array().head should equal(71) // capital "G", detects the gif
  }

  "GET /another/page with html" should "respond with html" in {
    get("/another/page", Map.empty, Map("Accept" -> "text/html"))
    response.body should equal("an html response")
  }

  "GET /another/page with json" should "respond with json" in {
    get("/another/page", Map.empty, Map("Accept" -> "application/json"))
    response.body should equal("an json response")
  }

  "GET /another/page with unsupported type" should "respond with catch all" in {
    get("/another/page", Map.empty, Map("Accept" -> "foo/bar"))
    response.body should equal("default fallback response")
  }

}
