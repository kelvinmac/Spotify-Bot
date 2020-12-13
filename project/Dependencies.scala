import sbt._
object Dependencies {

  object Circe {
    private val version = "0.12.3"

    lazy val circeCore    = "io.circe" %% "circe-core"    % version
    lazy val circeGeneric = "io.circe" %% "circe-generic" % version
    lazy val circeParse   = "io.circe" %% "circe-parser"  % version
  }

  object Cats {
    lazy val cats           = "org.typelevel" %% "cats-core"       % "2.1.1"
    lazy val catsEffect     = "org.typelevel" %% "cats-effect"     % "2.1.4"
    lazy val catsBird       = "io.catbird"    %% "catbird-finagle" % "20.10.0"
    lazy val catsBirdEffect = "io.catbird"    %% "catbird-effect"  % "20.10.0"
  }

  object Logging {
    lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
  }

  object Time {
    lazy val nScalaTime = "com.github.nscala-time" %% "nscala-time" % "2.24.0"
  }

  object Config {
    lazy val pureConfig = "com.github.pureconfig" %% "pureconfig" % "0.13.0"
  }

  object Finagle {
    private lazy val version = "0.32.1"

    lazy val finchCore   = "com.github.finagle" %% "finchx-core"  % version
    lazy val finchCirce  = "com.github.finagle" %% "finchx-circe" % version
    lazy val finagleHttp = "com.twitter"        %% "finagle-http" % "20.10.0"
  }

  lazy val testFramework = "org.scalatest" %% "scalatest" % "3.2.0" % "test"
}
