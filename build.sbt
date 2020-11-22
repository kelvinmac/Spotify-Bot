import sbt._
import Dependencies._

name := "Spotify-Bot"

version := "0.1"

scalaVersion := "2.13.3"

lazy val commonSettings = Seq(
  organization := "com.kelvin.spotify",
  scalacOptions += "-Ypartial-unification",
  resolvers += "Confluent" at "https://packages.confluent.io/maven/",
  addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full),
  libraryDependencies ++= List(
    Circe.circeCore,
    Circe.circeGeneric,
    Circe.circeParse,
    Cats.cats,
    Cats.catsEffect,
    Cats.catsBird,
    Cats.catsBirdEffect,
    Logging.scalaLogging,
    Time.nScalaTime,
    testFramework
  ),
  publish := {}
)

lazy val domain = (project in file("./domain"))
  .settings(commonSettings)
  .settings(
    name := "domain",
    libraryDependencies ++= List(
      Finagle.finagleCore
    )
  )

lazy val testSupport = (project in file("./test-support"))
  .dependsOn(domain)
  .settings(commonSettings)
  .settings(
    name := "Test-Support",
    libraryDependencies ++= List(
      Finagle.finchCore,
      Finagle.finchCirce,
      Finagle.finagleCore
    )
  )

lazy val application = (project in file("./app"))
  .dependsOn(domain)
  .settings(commonSettings)
  .settings(
    name := "Application",
    libraryDependencies ++= List(
      Config.pureConfig
    )
  )

lazy val service = (project in file("./service"))
  .dependsOn(domain, testSupport)
  .settings(commonSettings)
  .settings(
    name := "Service",
    libraryDependencies ++= List(
      Config.pureConfig,
      Finagle.finchCore,
      Finagle.finchCirce,
      Finagle.finagleCore
    )
  )
