name := "ShoppingCart"

version := "1.0.6"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "commons-io" % "commons-io" % "1.3.2",
  "com.twitter" %% "twitter-server" % "1.6.1",
  "com.twitter" %% "finagle-stats" % "6.13.1",
  "com.twitter" %% "finatra" % "1.5.3",
  "com.typesafe.slick" %% "slick" % "2.1.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.h2database" % "h2" % "1.4.182"
)

resolvers ++= Seq(
    "Twitter Maven" at "http://maven.twttr.com",
    "Finatra Repo" at "http://twitter.github.com/finatra"
)
